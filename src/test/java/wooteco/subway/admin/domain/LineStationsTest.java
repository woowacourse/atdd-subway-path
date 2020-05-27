package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.LinkedList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LineStationsTest {
    private static LineStations lineStations;

    @BeforeAll
    static void initialize() {
        LinkedList<LineStation> tempLineStations = new LinkedList<>();

        tempLineStations.add(new LineStation(null, 1L, 10, 10));
        tempLineStations.add(new LineStation(1L, 2L, 10, 10));
        tempLineStations.add(new LineStation(2L, 3L, 10, 10));
        tempLineStations.add(new LineStation(3L, 4L, 10, 10));
        tempLineStations.add(new LineStation(4L, 5L, 10, 10));
        tempLineStations.add(new LineStation(5L, 6L, 10, 10));
        tempLineStations.add(new LineStation(6L, 7L, 10, 10));
        tempLineStations.add(new LineStation(7L, 8L, 10, 10));

        lineStations = new LineStations(tempLineStations);
    }

    @Test
    @DisplayName("LineStations에서 모든 아이디를 가져오기 테스트")
    void getAllLineStationId() {
        Set<Long> allLineStationId = lineStations.getAllLineStationId();

        for (long i = 1L; i < 7L; i++) {
            assertThat(allLineStationId.contains(i));
        }
    }

    @ParameterizedTest
    @CsvSource(value = {"1,2,2", "2,3,3", "3,4,4", "4,5,5", "5,6,6", "6,7,7", "7,8,8"})
    @DisplayName("LineStations에서 전 역의 아이디와 현 역의 아이디로 LineStation 가져오기 테스트")
    void findLineStation(Long preStationId, Long stationId, Long expected) {
        assertThat(lineStations.findLineStation(preStationId, stationId).getStationId()).isEqualTo(expected);
    }

}