package wooteco.subway.admin.domain.line.path;

import org.jgrapht.WeightedGraph;

public enum EdgeWeightType implements EdgeWeightStrategy {
    DISTANCE {
        @Override
        public void setWeight(WeightedGraph<Long, RouteEdge> graph, RouteEdge edge) {
            graph.setEdgeWeight(edge, edge.getDistance());
        }
    },
    DURATION {
        @Override
        public void setWeight(WeightedGraph<Long, RouteEdge> graph, RouteEdge edge) {
            graph.setEdgeWeight(edge, edge.getDuration());
        }
    }
}
