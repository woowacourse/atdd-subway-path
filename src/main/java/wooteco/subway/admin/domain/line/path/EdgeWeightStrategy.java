package wooteco.subway.admin.domain.line.path;

import org.jgrapht.WeightedGraph;

@FunctionalInterface
public interface EdgeWeightStrategy {
    void setWeight(WeightedGraph<Long, RouteEdge> graph, RouteEdge edge);
}
