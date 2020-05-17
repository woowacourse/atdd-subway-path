package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotConnectEdgeException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPathByDistance(PathRequest pathRequest) {
        final Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(toMap(Station::getId, station -> station));
        final List<Line> lines = lineRepository.findAll();
        final Station startStation = getStationWithValidation(stations, pathRequest.getSource());
        final Station endStation = getStationWithValidation(stations, pathRequest.getTarget());

        final DijkstraShortestPath<Station, Edge> dijkstraShortestPath =
            new DijkstraShortestPath<>(makeGraph(stations, lines, pathRequest.getType()));

        final GraphPath<Station, Edge> path = dijkstraShortestPath.getPath(startStation, endStation);

        if (Objects.isNull(path)) {
            throw new NotConnectEdgeException();
        }

        final List<Station> shortestPath = path.getVertexList();
        final List<Edge> edgeList = path.getEdgeList();

        final int distance = edgeList.stream().mapToInt(Edge::getDistance).sum();
        final int duration = edgeList.stream().mapToInt(Edge::getDuration).sum();

        return new PathResponse(StationResponse.listOf(shortestPath), distance, duration);
    }

    private Station getStationWithValidation(Map<Long, Station> stations, Long stationId) {
        if (!stations.containsKey(stationId)) {
            throw new NoSuchElementException("등록되어있지 않은 역입니다.");
        }

        return stations.get(stationId);
    }

    private WeightedMultigraph<Station, Edge> makeGraph(Map<Long, Station> stations, List<Line> lines,
        PathType pathType) {
        final WeightedMultigraph<Station, Edge> graph
            = new WeightedMultigraph<>(Edge.class);

        stations.values()
            .forEach(graph::addVertex);

        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .forEach(lineStation -> {
                Station preStation = getStationWithValidation(stations, lineStation.getPreStationId());
                Station currentStation = getStationWithValidation(stations, lineStation.getStationId());
                Edge edge = lineStation.toEdge();

                graph.addEdge(preStation, currentStation, edge);
                graph.setEdgeWeight(edge, pathType.getValue(lineStation));
            });
        return graph;
    }
}