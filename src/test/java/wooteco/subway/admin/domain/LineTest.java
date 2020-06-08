package wooteco.subway.admin.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.admin.service.errors.PathException;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    @Test
    void addLineStation() {
        line.addLineStation(new LineStation(null, 4L, 10, 10));

        assertThat(line.getStations()).hasSize(4);
        LineStation lineStation = line.getStations().stream()
                .filter(it -> it.getStationId() == 4L)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        assertThat(lineStation.getStationId()).isEqualTo(4L);
    }

    @Test
    void getLineStations() {
        List<Long> stationIds = line.getLineStationsId();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeLineStation(Long stationId) {
        line.removeLineStationById(stationId);

        assertThat(line.getStations()).hasSize(2);
    }

    @Test
    @DisplayName("시작노선역을 제외한 나머지 노선역들이 리스트형태로 반환된다")
    void lineStationsWithOutSourceLineStationTest() {
        List<LineStation> lineStations = line.lineStationsWithOutSourceLineStation();
        Set<LineStation> stations = line.getStations();
        assertThat(lineStations.size()).isEqualTo(stations.size() - 1);
    }

    @Test
    @DisplayName("노선의 정보가 갱신된다 ")
    void updateLineTest() {
        Line line = new Line("1호선", LocalTime.now(), LocalTime.now(), 10);
        Line updateLine = new Line("2호선", LocalTime.now(), LocalTime.now(), 20);
        line.update(updateLine);

        assertThat(line.getName()).isEqualTo("2호선");
    }

    @Test
    @DisplayName("역이 연결 안되있을 경우 익셉션이 발생한다.")
    void pathExceptionTest() {
        Line line = new Line("1호선", LocalTime.now(), LocalTime.now(), 10);
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));

        Assertions.assertThatThrownBy(() -> line.getLineStationsId())
                .isInstanceOf(PathException.class)
                .hasMessage("역이 연결되있지 않습니다.");
    }
}
