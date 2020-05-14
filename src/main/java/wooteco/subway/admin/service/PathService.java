package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
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

    public PathResponse findShortestPathByDistance(Long startId, Long endId) {
        final Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(toMap(Station::getId, station -> station));
        final List<Line> lines = lineRepository.findAll();
        final Station startStation = stations.get(startId);
        final Station endStation = stations.get(endId);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(makeGraph(stations, lines));

        List<Station> shortestPath = dijkstraShortestPath.getPath(startStation, endStation).getVertexList();
        double distance = dijkstraShortestPath.getPath(startStation, endStation).getWeight();

        return new PathResponse(shortestPath, (int) distance, 40);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(Map<Long, Station> stations, List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stations.values()
            .forEach(graph::addVertex);

        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .forEach(lineStation -> {
                Station preStation = stations.get(lineStation.getPreStationId());
                Station currentStation = stations.get(lineStation.getStationId());

                graph.setEdgeWeight(graph.addEdge(preStation, currentStation), lineStation.getDistance());
            });
        return graph;
    }
}
