package wooteco.subway.admin.domain.line.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;
import wooteco.subway.admin.domain.line.LineStation;
import wooteco.subway.admin.domain.line.LineStations;

class SubwayRouteTest {

    private SubwayRoute distanceRoute;
    private SubwayRoute durationRoute;
    @BeforeEach
    void setUp() {
        LineStation lineStation = new LineStation(null, 1L, 5, 10);
        LineStation lineStation1 = new LineStation(1L, 2L, 5, 10);
        LineStation lineStation2 = new LineStation(2L, 3L, 5, 10);
        LineStations lineStations = new LineStations(
            Sets.newHashSet(lineStation, lineStation1, lineStation2));
        SubwayGraph subwayGraph = lineStations.toGraph((graph, edge) -> graph.setEdgeWeight(edge, edge.getDistance()));
        SubwayGraph subwayGraph1 = lineStations.toGraph((graph, edge) -> graph.setEdgeWeight(edge, edge.getDuration()));

        distanceRoute = subwayGraph.findDijkstraShortestPath(1L, 3L);

        durationRoute = subwayGraph1.findDijkstraShortestPath(1L, 3L);
    }

    @Test
    void getShortestPath() {
        assertThat(distanceRoute.getShortestPath().size()).isEqualTo(3);
        assertThat(durationRoute.getShortestPath().size()).isEqualTo(3);
    }

    @Test
    void calculateTotalDistance() {
        assertThat(distanceRoute.calculateTotalDistance()).isEqualTo(10);
        assertThat(durationRoute.calculateTotalDistance()).isEqualTo(10);
    }

    @Test
    void calculateTotalDuration() {
        assertThat(distanceRoute.calculateTotalDuration()).isEqualTo(20);
        assertThat(durationRoute.calculateTotalDuration()).isEqualTo(20);
    }
}