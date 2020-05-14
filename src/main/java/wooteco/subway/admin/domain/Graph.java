package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.exception.NotExistPathException;

import java.util.List;
import java.util.Map;

public class Graph {
    private WeightedMultigraph<Station, LineStationEdge> graph;

    private Graph(WeightedMultigraph<Station, LineStationEdge> graph) {
        this.graph = graph;
    }

    public static Graph of(Map<Long, Station> stations, List<LineStation> edges, PathType pathType) {
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
        return new Graph(graph);
    }

    GraphPath<Station, LineStationEdge> getShortestPath(Station source, Station target) {
        GraphPath<Station, LineStationEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
        if (path == null) {
            throw new NotExistPathException(String.format("(%s, %s) 구간이 존재하지 않습니다.", source.getName(), target.getName()));
        }
        return path;
    }
}
