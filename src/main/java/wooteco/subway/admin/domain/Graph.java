package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Graph {
    public Graph() {
    }

    public static List<Long> findPath(Lines lines, Long sourceId, Long targetId, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.addVertex(graph);
        lines.setEdgeWeight(graph, type);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
    }
}
