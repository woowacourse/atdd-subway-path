package wooteco.subway.admin.domain;

import org.jgrapht.WeightedGraph;

@FunctionalInterface
public interface EdgeWeightStrategy {
    void setWeight(WeightedGraph<Long, RouteEdge> graph, RouteEdge edge);
}
