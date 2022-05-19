package wooteco.subway.domain.path.strategy;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import wooteco.subway.domain.path.PathEdge;

public interface PathFindingStrategy {
    GraphPath<Long, PathEdge> findPathBetween(Graph<Long, PathEdge> graph, Long source, Long target);
}
