package wooteco.subway.admin.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class LineStationTest {

    @Test
    void isTest() {
        LineStation lineStation = new LineStation(1L, 2L, 10, 10);

        assertTrue(lineStation.isEdgeOf(1L, 2L));
    }
}
