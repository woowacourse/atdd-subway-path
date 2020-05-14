package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPathByDistance(PathRequest pathRequest) {

        final Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(toMap(Station::getId, station -> station));
        final List<Line> lines = lineRepository.findAll();
        final Station startStation = stations.get(pathRequest.getSource());
        final Station endStation = stations.get(pathRequest.getTarget());

        DijkstraShortestPath<Station, Edge> dijkstraShortestPath =
            new DijkstraShortestPath<>(makeGraph(stations, lines));

        final List<Edge> edgeList = dijkstraShortestPath.getPath(startStation, endStation).getEdgeList();
        final List<Station> shortestPath = dijkstraShortestPath.getPath(startStation, endStation).getVertexList();

        final int distance = edgeList.stream().mapToInt(Edge::getDistance).sum();
        final int duration = edgeList.stream().mapToInt(Edge::getDuration).sum();

        return new PathResponse(shortestPath, distance, duration);
    }

    private WeightedMultigraph<Station, Edge> makeGraph(Map<Long, Station> stations, List<Line> lines) {
        WeightedMultigraph<Station, Edge> graph
            = new WeightedMultigraph<>(Edge.class);

        stations.values()
            .forEach(graph::addVertex);

        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .forEach(lineStation -> {
                Station preStation = stations.get(lineStation.getPreStationId());
                Station currentStation = stations.get(lineStation.getStationId());

                Edge edge = lineStation.toEdge();
                graph.addEdge(preStation, currentStation, edge);
                graph.setEdgeWeight(edge, lineStation.getDistance());
            });
        return graph;
    }
}
