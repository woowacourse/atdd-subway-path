package wooteco.subway.admin.domain.line.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;
import wooteco.subway.admin.domain.line.LineStation;
import wooteco.subway.admin.domain.line.LineStations;

class SubwaySubwayRouteTest {

    private Path distanceRoute;
    private Path durationRoute;

    @BeforeEach
    void setUp() {
        LineStation lineStation = new LineStation(null, 1L, 5, 10);
        LineStation lineStation1 = new LineStation(1L, 2L, 5, 10);
        LineStation lineStation2 = new LineStation(2L, 3L, 5, 10);
        LineStations lineStations = new LineStations(
            Sets.newHashSet(lineStation, lineStation1, lineStation2));
        SubwayMap subwayMap = lineStations.toGraph(RouteEdge::getDistance);
        SubwayMap subwayMap1 = lineStations.toGraph(RouteEdge::getDuration);

        distanceRoute = subwayMap.findShortestPath(1L, 3L);

        durationRoute = subwayMap1.findShortestPath(1L, 3L);
    }

    @Test
    void getShortestPath() {
        assertThat(distanceRoute.getPath().size()).isEqualTo(3);
        assertThat(durationRoute.getPath().size()).isEqualTo(3);
    }

    @Test
    void createPathInfo() {
        assertThat(distanceRoute.createPathInfo().getTotalDistance()).isEqualTo(10);
        assertThat(durationRoute.createPathInfo().getTotalDistance()).isEqualTo(10);
        assertThat(distanceRoute.createPathInfo().getTotalDuration()).isEqualTo(20);
        assertThat(durationRoute.createPathInfo().getTotalDuration()).isEqualTo(20);
    }
}