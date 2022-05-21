package wooteco.subway.domain.path;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class DijkstraStrategy implements PathFindingStrategy {
    public int calculateShortestDistance(Graph<Long, DefaultWeightedEdge> graph,Long source, Long target) {
        return (int) findShortestDijkstraPath(graph, source, target).getWeight();
    }

    public List<Long> getShortestPath(Graph<Long, DefaultWeightedEdge> graph, Long source, Long target) {
        return findShortestDijkstraPath(graph, source, target).getVertexList();
    }

    private GraphPath<Long, DefaultWeightedEdge> findShortestDijkstraPath(Graph<Long, DefaultWeightedEdge> graph, Long source, Long target) {
        GraphPath<Long, DefaultWeightedEdge> graphPath = DijkstraShortestPath.findPathBetween(graph, source, target);

        if (graphPath == null) {
            throw new IllegalArgumentException("최단 경로를 찾을 수 없습니다.");
        }
        return graphPath;
    }
}
