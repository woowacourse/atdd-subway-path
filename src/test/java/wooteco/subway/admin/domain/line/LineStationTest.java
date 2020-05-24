package wooteco.subway.admin.domain.line;

import static org.assertj.core.api.Assertions.*;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.line.path.RouteEdge;

class LineStationTest {
    @Test
    void addEdgeToGraph() {
        Long departureId = 1L;
        Long arrivalId = 2L;
        LineStation lineStation = new LineStation(departureId, arrivalId, 10, 10);

        WeightedGraph<Long, RouteEdge> graph = new WeightedMultigraph<>(RouteEdge.class);
        graph.addVertex(departureId);
        graph.addVertex(arrivalId);

        lineStation.addEdgeTo(graph, (g, edge) -> g.setEdgeWeight(edge, edge.getDistance()));
        RouteEdge created = graph.getEdge(1L, 2L);

        assertThat(created.getDistance()).isEqualTo(lineStation.getDistance());
        assertThat(created.getDuration()).isEqualTo(lineStation.getDuration());
    }
}