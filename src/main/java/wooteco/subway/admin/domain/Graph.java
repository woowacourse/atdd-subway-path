package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.exception.NotExistPathException;

import java.util.List;
import java.util.Map;

public class Graph {
    private WeightedMultigraph<Station, LineStationEdge> graph;
    private CustomShortestPathAlgorithm shortestPathAlgorithm;

    public Graph(WeightedMultigraph<Station, LineStationEdge> graph, CustomShortestPathAlgorithm shortestPathAlgorithm) {
        this.graph = graph;
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public static Graph of(Map<Long, Station> stations, List<LineStation> edges, PathType pathType,
                           CustomShortestPathAlgorithm shortestPathAlgorithm) {
        WeightedMultigraph<Station, LineStationEdge> graph = new WeightedMultigraph<>(LineStationEdge.class);

        for (Station station : stations.values()) {
            graph.addVertex(station);
        }

        edges.stream()
                .filter(edge -> edge.getPreStationId() != null)
                .forEach(edge -> {
                    Station sourceVertex = stations.get(edge.getPreStationId());
                    Station targetVertex = stations.get(edge.getStationId());
                    graph.addEdge(sourceVertex, targetVertex, LineStationEdge.of(edge, pathType));
                });
        return new Graph(graph, shortestPathAlgorithm);
    }

    GraphPath<Station, LineStationEdge> getShortestPath(Station source, Station target) {
        GraphPath<Station, LineStationEdge> path = shortestPathAlgorithm.getPath(graph, source, target);
        if (path == null) {
            throw new NotExistPathException(source.getName(), target.getName());
        }
        return path;
    }
}
