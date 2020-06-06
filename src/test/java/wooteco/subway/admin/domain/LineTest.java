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

import wooteco.subway.admin.exception.DisconnectedStationException;
import wooteco.subway.admin.exception.NotFoundStationException;

public class LineTest {
    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    @Test
    @DisplayName("노선의 첫번째 역으로 추가")
    void addLineStation() {
        line.addLineStation(new LineStation(null, 4L, 10, 10));

        assertThat(line.getStations()).hasSize(4);
        LineStation lineStation = line.getStations().stream()
            .filter(it -> Objects.equals(it.getPreStationId(), 4L))
            .findFirst()
            .orElseThrow(RuntimeException::new);
        assertThat(lineStation.getStationId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("노선에 추가된 역 정보를 조회")
    void getLineStations() {
        List<Long> stationIds = line.getLineStationsId();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }

    @ParameterizedTest
    @DisplayName("역의 id로 해당 역을 삭제")
    @ValueSource(longs = {1L, 2L, 3L})
    void removeLineStation(Long stationId) {
        line.removeLineStationById(stationId);

        assertThat(line.getStations()).hasSize(2);
    }

    @ParameterizedTest
    @DisplayName("삭제할 역이 존재하지 않는 경우 예외 발생")
    @ValueSource(longs = {4L, 5L, 6L})
    void removeLineStation_NotFoundTargetToRemove(Long stationId) {
        assertThatThrownBy(() -> line.removeLineStationById(stationId)).isInstanceOf(
            NotFoundStationException.class)
            .hasMessage(String.format("id가 %d인 역이 존재하지 않습니다", stationId));
    }

    @Test
    @DisplayName("첫번째 역이 존재하지 않는 경우 예외 발생")
    void getLineStationsId_NotFoundFirstStation() {
        //given
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
        //when & then
        assertThatThrownBy(line::getLineStationsId)
            .isInstanceOf(NotFoundStationException.class)
            .hasMessage("첫번째 지하철역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("중간에 역이 연결되어 있지 않는 경우 예외 발생")
    void name() {
        //given
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(3L, 4L, 10, 10));
        //when & then
        assertThatThrownBy(line::getLineStationsId)
            .isInstanceOf(DisconnectedStationException.class)
            .hasMessage(String.format("id가 %d인 역의 다음역이 존재하지 않습니다", 2L));
    }
}
