package wooteco.subway.domain.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.TestFixture.STATIONS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RouteTest {

    private static final int ROUTE_DISTANCE = 10;
    private Route route;

    @BeforeEach
    void setup() {
        route = new Route(STATIONS, ROUTE_DISTANCE);
    }

    @DisplayName("경로를 반환한다")
    @Test
    void getShortestRoute() {
        assertThat(route.getRoute()).isEqualTo(STATIONS);
    }

    @DisplayName("거리를 반환한다")
    @Test
    void getRouteDistance() {
        assertThat(route.getDistance()).isEqualTo(ROUTE_DISTANCE);
    }
}
