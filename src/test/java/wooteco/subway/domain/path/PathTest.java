package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.InvalidPathException;

class PathTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private WeightStrategy strategy;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");

        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));

        strategy = WeightType.findStrategy("DISTANCE");
    }

    @DisplayName("각 구간 사이가 10분이고, 경로의 총 소요 시간이 20분인 경우 duration 확인 테스트")
    @Test
    void duration() {
        //given
        double expected = 20;

        //when
        Path path = new Path(Arrays.asList(line),
            Arrays.asList(station1, station2, station3, station4), STATION_NAME1, STATION_NAME3,
            strategy);
        double actual = path.duration();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("연결되지 않은 역의 경로를 요청 시, 예외 발생 테스트")
    @Test
    void name() {
        //given
        Line line2 = new Line();
        line2.addLineStation(new LineStation(null, station4.getId(), 0, 0));
        List<Line> lines = Arrays.asList(line, line2);
        List<Station> stations = Arrays.asList(station1, station2, station3, station4);

        //when //then
        assertThatThrownBy(() -> new Path(lines, stations, STATION_NAME1, STATION_NAME4, strategy))
            .isInstanceOf(InvalidPathException.class)
            .hasMessage(String.format("경로를 찾을 수 없습니다. sourceName: %s target: %s", STATION_NAME1,
                STATION_NAME4));
    }
}
