package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Graph {

    private final WeightedMultigraph<Long, LineStationEdge> graph;

    public Graph(WeightedMultigraph<Long, LineStationEdge> graph) {
        this.graph = graph;
    }

    public static Graph of(List<Line> lines, Map<Long, Station> stationMatcher, PathType pathType) {
        WeightedMultigraph<Long, LineStationEdge> graph = new WeightedMultigraph<>(
            LineStationEdge.class);
        addStationsAsVertex(stationMatcher, graph);
        addEdge(lines, pathType, graph);
        return new Graph(graph);
    }

    private static void addEdge(List<Line> lines, PathType pathType,
        WeightedMultigraph<Long, LineStationEdge> graph) {
        for (Line line : lines) {
            for (LineStation lineStation : line.getStations()) {
                if (Objects.isNull(lineStation.getPreStationId())) {
                    continue;
                }
                LineStationEdge lineStationEdge = LineStationEdge.of(lineStation);
                graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(),
                    lineStationEdge);
                graph.setEdgeWeight(lineStationEdge, pathType.getWeight(lineStation));
            }
        }
    }

    private static void addStationsAsVertex(Map<Long, Station> stationMatcher,
        WeightedMultigraph<Long, LineStationEdge> graph) {
        for (Station station : stationMatcher.values()) {
            graph.addVertex(station.getId());
        }
    }

    public Path getPath(Long source, Long target) {
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);
        GraphPath<Long, LineStationEdge> path = dijkstraShortestPath.getPath(source, target);
        return Path.of(path);
    }

}
