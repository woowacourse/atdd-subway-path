package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Set<Long> allLineStationId = lineStations.getAllStationId();

        for (long i = 1L; i < 7L; i++) {
            assertThat(allLineStationId.contains(i));
        }
    }

    @Test
    @DisplayName("LineStations에서 전 역의 아이디와 현 역의 아이디로 LineStation 가져오기 테스트")
    void findLineStation() {
        assertThat(lineStations.findLineStation(null, 1L).getStationId()).isEqualTo(1L);
        assertThat(lineStations.findLineStation(1L, 2L).getStationId()).isEqualTo(2L);
        assertThat(lineStations.findLineStation(2L, 3L).getStationId()).isEqualTo(3L);
        assertThat(lineStations.findLineStation(3L, 4L).getStationId()).isEqualTo(4L);
        assertThat(lineStations.findLineStation(4L, 5L).getStationId()).isEqualTo(5L);
        assertThat(lineStations.findLineStation(5L, 6L).getStationId()).isEqualTo(6L);
        assertThat(lineStations.findLineStation(6L, 7L).getStationId()).isEqualTo(7L);
        assertThat(lineStations.findLineStation(7L, 8L).getStationId()).isEqualTo(8L);
    }

}