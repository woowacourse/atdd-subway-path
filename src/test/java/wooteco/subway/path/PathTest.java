package wooteco.subway.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {
    Station 강남역 = new Station(1L, "강남역");
    Station 양재역 = new Station(2L, "양재역");;
    Station 교대역 = new Station(3L, "교대역");;
    Station 남부터미널역 = new Station(4L, "남부터미널역");;

    Line 이호선 = new Line("이호선", "green");
    Line 삼호선 = new Line("삼호선", "brown");
    Line 신분당선 = new Line("신분당선", "pink");

    Path path;

    @BeforeEach
    void setUp() {
        이호선.addSection(교대역, 강남역, 10);
        삼호선.addSection(교대역, 남부터미널역, 3);
        삼호선.addSection(남부터미널역, 양재역, 2);
        신분당선.addSection(강남역, 양재역, 10);

        List<Station> stations = Arrays.asList(강남역, 양재역, 교대역, 남부터미널역);
        List<Line> lines = Arrays.asList(이호선, 삼호선);

        path = new Path(stations, lines);
    }

    /**
     * 교대역    --- (10) *2호선*   ---   강남역
     * |                                  |
     *(3) *3호선*                    (10) *신분당선*
     * |                                  |
     * 남부터미널역  --- (2) *3호선* ---   양재
     */
    @Test
    @DisplayName("최단 경로를 잘 구하는지 확인한다.")
    void findShortestPath() {
        List<Station> expectedPath = Arrays.asList(교대역, 남부터미널역, 양재역);
        assertThat(path.findShortestPath(교대역, 양재역)).isEqualTo(expectedPath);
    }

    @Test
    @DisplayName("최단 거리를 잘 구하는지 확인한다.")
    void findShortestDistance() {
        assertThat(path.findShortestDistance(교대역, 양재역)).isEqualTo(5.0);
    }
}
