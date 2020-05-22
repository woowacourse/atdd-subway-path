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

public class GraphTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "양재역";

    private Line line;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
        line.addLineStation(new LineStation(null, 1L, 0, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 1));
        line.addLineStation(new LineStation(2L, 3L, 10, 1));
        line.addLineStation(new LineStation(3L, 4L, 10, 1));

        line2 = new Line(2L, "4호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-blue-600");
        line2.addLineStation(new LineStation(1L, 5L, 10, 10));
        line2.addLineStation(new LineStation(5L, 4L, 10, 10));
    }

    @DisplayName("최단 거리 기준 경로 찾는 기능 테스트")
    @Test
    void createPath_GivenDistanceStrategy_CreatePath() {
        //given
        WeightStrategy distanceStrategy = WeightType.DISTANCE.getStrategy();
        Graph graph = new Graph(Arrays.asList(line, line2),
            Arrays.asList(station1, station2, station3, station4, station5), distanceStrategy);

        //when
        Path path = graph.createPath(station1, station4);

        //then
        assertThat(path.getVertexList()).contains(station1, station5, station4);
    }

    @DisplayName("최소 시간 기준 경로 찾는 기능 테스트")
    @Test
    void createPath_GivenDurationStrategy_CreatePath() {
        //given
        WeightStrategy durationStrategy = WeightType.DURATION.getStrategy();
        Graph graph = new Graph(Arrays.asList(line, line2),
            Arrays.asList(station1, station2, station3, station4, station5), durationStrategy);

        //when
        Path path = graph.createPath(station1, station4);

        //then
        assertThat(path.getVertexList()).contains(station1, station2, station3, station4);
    }

    @DisplayName("예외테스트: 연결되지 않은 역의 경로를 요청 시, 예외 발생")
    @Test
    void createPath_GivenNotConnectedPath_ExceptionThrown() {
        //given
        Line line3 = new Line();
        Station station6 = new Station(6L, "잠실역");
        line3.addLineStation(new LineStation(null, station6.getId(), 0, 0));

        List<Line> lines = Arrays.asList(line, line3);
        List<Station> stations = Arrays.asList(station1, station2, station3, station4, station6);
        WeightStrategy strategy = WeightType.DISTANCE.getStrategy();

        Graph graph = new Graph(lines, stations, strategy);

        //when //then
        assertThatThrownBy(() -> graph.createPath(station1, station6))
            .isInstanceOf(InvalidPathException.class)
            .hasMessage(
                String.format("경로를 찾을 수 없습니다. sourceName: %s target: %s", station1.getName(),
                    station6.getName()));
    }
}
