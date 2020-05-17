package wooteco.subway.admin.domain.line;

import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.line.path.RouteEdge;

import static org.assertj.core.api.Assertions.assertThat;

class LineStationTest {
    @Test
    void isNotStart() {
        LineStation lineStation = new LineStation(1L, 2L, 10, 10);
        assertThat(lineStation.isNotStart()).isTrue();
    }

    @Test
    void toEdge() {
        LineStation lineStation = new LineStation(1L, 2L, 10, 10);
        assertThat(lineStation.toEdge()).isInstanceOf(RouteEdge.class);
    }
}