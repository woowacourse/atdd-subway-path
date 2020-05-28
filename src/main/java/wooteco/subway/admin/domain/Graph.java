package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Graph {
    private final WeightedMultigraph<Long, LineStationEdge> graph;

    public Graph(WeightedMultigraph<Long, LineStationEdge> graph) {
        this.graph = graph;
    }

    public void addVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
    }

    public void readyToEdge(List<Line> lines, PathType pathType) {
        List<LineStation> lineStations = lines.stream()
            .flatMap(line -> line.getStations().stream())
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .collect(Collectors.toList());
        for (LineStation lineStation : lineStations) {
            LineStationEdge lineStationEdge = LineStationEdge.of(lineStation);
            graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), lineStationEdge);
            graph.setEdgeWeight(lineStationEdge, pathType.getWeight(lineStation));
        }
    }

    public Path createPath(Long sourceId, Long targetId) {
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return new Path(dijkstraShortestPath.getPath(sourceId, targetId));
    }
}
