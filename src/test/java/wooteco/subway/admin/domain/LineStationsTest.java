package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LineStationsTest {
    private LineStations lineStations;

    @BeforeEach
    void setUp() {
        lineStations = new LineStations();
        lineStations.addLineStation(new LineStation(null, 1L, 10, 10));
        lineStations.addLineStation(new LineStation(1L, 3L, 10, 10));
        lineStations.addLineStation(new LineStation(3L, 2L, 10, 10));
    }

    @DisplayName("노선에 역 추가")
    @Test
    void addLineStation() {
        lineStations.addLineStation(new LineStation(null, 4L, 10, 10));

        assertThat(lineStations.getLineStations()).hasSize(4);
        LineStation lineStation = lineStations.getLineStations().stream()
            .filter(it -> Objects.equals(it.getPreStationId(), 4L))
            .findFirst()
            .orElseThrow(RuntimeException::new);
        assertThat(lineStation.getStationId()).isEqualTo(1L);
    }

    @DisplayName("노선에 있는 역 제거")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeLineStation(Long stationId) {
        lineStations.removeLineStationById(stationId);

        assertThat(lineStations.getLineStations()).hasSize(2);
    }

    @DisplayName("노선에 속한 정렬된 역 조회")
    @Test
    void getSortedLineStations() {
        List<LineStation> sortedLineStations = lineStations.createSortedLineStations();

        assertThat(sortedLineStations.size()).isEqualTo(3);
        assertThat(sortedLineStations.get(0).getStationId()).isEqualTo(1L);
        assertThat(sortedLineStations.get(1).getStationId()).isEqualTo(3L);
        assertThat(sortedLineStations.get(2).getStationId()).isEqualTo(2L);
    }

    @DisplayName("노선에 속한 정렬된 역 id 조회")
    @Test
    void getSortedLineStationIds() {
        List<Long> sortedLineStationIds = lineStations.createSortedLineStationsId();

        assertThat(sortedLineStationIds.size()).isEqualTo(3);
        assertThat(sortedLineStationIds.get(0)).isEqualTo(1L);
        assertThat(sortedLineStationIds.get(1)).isEqualTo(3L);
        assertThat(sortedLineStationIds.get(2)).isEqualTo(2L);
    }
}