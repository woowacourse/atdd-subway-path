package wooteco.subway.domain.path.strategy;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.domain.path.PathEdge;

public class DijkstraStrategy implements PathFindingStrategy {
    public GraphPath<Long, PathEdge> findPathBetween(Graph<Long, PathEdge> graph, Long source, Long target) {
        return DijkstraShortestPath.findPathBetween(graph, source, target);
    }
}
