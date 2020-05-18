package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LineTest {
    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 3L, 10, 10));
        line.addLineStation(new LineStation(3L, 2L, 10, 10));
    }

    @DisplayName("역 업데이트")
    @Test
    void update() {
        Line updatedLine = new Line("3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");
        line.update(updatedLine);

        assertThat(line.getName()).isEqualTo("3호선");
    }

    @DisplayName("노선에 역 추가")
    @Test
    void addLineStation() {
        line.addLineStation(new LineStation(null, 4L, 10, 10));

        assertThat(line.getStations()).hasSize(4);
        LineStation lineStation = line.getStations().stream()
            .filter(it -> Objects.equals(it.getPreStationId(), 4L))
            .findFirst()
            .orElseThrow(RuntimeException::new);
        assertThat(lineStation.getStationId()).isEqualTo(1L);
    }

    @DisplayName("노선에 있는 역 제거")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeLineStation(Long stationId) {
        line.removeLineStationById(stationId);

        assertThat(line.getStations()).hasSize(2);
    }

    @DisplayName("노선에 속한 정렬된 역 조회")
    @Test
    void getSortedLineStations() {
        List<LineStation> lineStations = line.getLineStations();

        assertThat(lineStations.size()).isEqualTo(3);
        assertThat(lineStations.get(0).getStationId()).isEqualTo(1L);
        assertThat(lineStations.get(1).getStationId()).isEqualTo(3L);
        assertThat(lineStations.get(2).getStationId()).isEqualTo(2L);
    }

    @DisplayName("노선에 속한 정렬된 역 id 조회")
    @Test
    void getSortedLineStationIds() {
        List<Long> lineStationIds = line.getLineStationsId();

        assertThat(lineStationIds.size()).isEqualTo(3);
        assertThat(lineStationIds.get(0)).isEqualTo(1L);
        assertThat(lineStationIds.get(1)).isEqualTo(3L);
        assertThat(lineStationIds.get(2)).isEqualTo(2L);
    }
}
