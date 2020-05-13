package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LineStationTest {

    @Test
    void toEdge() {
        LineStation lineStation = new LineStation(1L, 2L, 10, 15);
        int distance = lineStation.toEdge().getDistance();
        int duration = lineStation.toEdge().getDuration();

        assertThat(distance).isEqualTo(lineStation.getDistance());
        assertThat(duration).isEqualTo(lineStation.getDuration());
    }
}