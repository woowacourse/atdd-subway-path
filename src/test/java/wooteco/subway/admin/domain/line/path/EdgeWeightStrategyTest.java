package wooteco.subway.admin.domain.line.path;

import static org.assertj.core.api.Assertions.*;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EdgeWeightStrategyTest {
    private WeightedGraph<Long, RouteEdge> graph;
    private RouteEdge routeEdge;

    @BeforeEach
    void setUp() {
        graph = new SimpleWeightedGraph<>(RouteEdge.class);
        routeEdge = new RouteEdge(10, 10);
        graph.addVertex(1L);
        graph.addVertex(2L);
        graph.addEdge(1L, 2L, routeEdge);
    }

    @Test
    void setWeight() {
        EdgeWeightStrategy edgeWeightStrategy = (graph, edge) -> graph.setEdgeWeight(edge, edge.getDuration());
        edgeWeightStrategy.setWeight(graph, routeEdge);
        assertThat(graph.getEdgeWeight(routeEdge)).isEqualTo(routeEdge.getDuration());
    }
}