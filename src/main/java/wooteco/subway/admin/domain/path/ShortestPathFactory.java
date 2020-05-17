package wooteco.subway.admin.domain.path;

import java.util.List;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import wooteco.subway.admin.domain.Edge;

public class ShortestPathFactory {

    public static ShortestPath createDijkstra(PathType pathType, List<Edge> edges) {
        WeightedGraph<Long, WeightedEdge> graph = createGraph(pathType, edges);
        DijkstraShortestPath<Long, WeightedEdge> path = new DijkstraShortestPath<>(graph);
        return ShortestPath.of(path);
    }

    private static WeightedGraph<Long, WeightedEdge> createGraph(PathType pathType,
        List<Edge> edges) {
        WeightedGraph<Long, WeightedEdge> graph
            = new DirectedWeightedMultigraph<>(WeightedEdge.class);

        for (Edge station : edges) {
            graph.addVertex(station.getStationId());
            if (station.isFirst()) {
                continue;
            }
            WeightedEdge weightedEdge
                = graph.addEdge(station.getPreStationId(), station.getStationId());
            weightedEdge.setSubWeight(pathType.findSubWeight(station));
            graph.setEdgeWeight(weightedEdge, pathType.findWeight(station));
        }

        return graph;
    }
}
