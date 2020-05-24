package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.NotConnectEdgeException;

public class Path {
    private final WeightedMultigraph<Station, Edge> graph;
    private final Map<Long, Station> stations;
    private final List<Line> lines;

    public Path(Map<Long, Station> stations, List<Line> lines) {
        this.graph = new WeightedMultigraph<>(Edge.class);
        this.stations = stations;
        this.lines = lines;
    }

    private WeightedMultigraph<Station, Edge> makeGraph(Map<Long, Station> stations, List<Line> lines,
        PathType pathType) {
        stations.values()
            .forEach(graph::addVertex);

        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .forEach(lineStation -> addEdgeAndSetEdgeWeight(stations, pathType, lineStation));

        return graph;
    }

    private void addEdgeAndSetEdgeWeight(Map<Long, Station> stations, PathType pathType, LineStation lineStation) {
        Station preStation = getStationWithValidation(stations, lineStation.getPreStationId());
        Station currentStation = getStationWithValidation(stations, lineStation.getStationId());
        Edge edge = lineStation.toEdge();

        graph.addEdge(preStation, currentStation, edge);
        graph.setEdgeWeight(edge, pathType.getValue(lineStation));
    }

    private Station getStationWithValidation(Map<Long, Station> stations, Long stationId) {
        if (!stations.containsKey(stationId)) {
            throw new NoSuchElementException("등록되어있지 않은 역입니다.");
        }

        return stations.get(stationId);
    }

    public GraphPath<Station, Edge> getGraphPath(Long source, Long target, PathType pathType) {
        final Station startStation = getStationWithValidation(stations, source);
        final Station endStation = getStationWithValidation(stations, target);

        final DijkstraShortestPath<Station, Edge> dijkstraShortestPath =
            new DijkstraShortestPath<>(makeGraph(stations, lines, pathType));

        final GraphPath<Station, Edge> path = dijkstraShortestPath.getPath(startStation, endStation);

        if (Objects.isNull(path)) {
            throw new NotConnectEdgeException();
        }

        return path;
    }
}