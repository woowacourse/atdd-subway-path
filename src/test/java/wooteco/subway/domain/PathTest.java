package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    private List<Line> lines;

    //            신사 (3호선)
    //            |10|
    // 신반포 (9호선) 잠원
    //       \10\ |10|
    // 내방 >10> 고속터미널 >10> 반포 >10> 논현 (7호선)
    //            |10| \14\
    //            서초 >3> 사평 (새호선)
    @BeforeEach
    void init() {
        Line line7 = new Line("7호선", "red",
            List.of(
                makeSection("내방역", "고속터미널역", 10),
                makeSection("고속터미널역", "반포역", 10),
                makeSection("반포역", "논현역", 10))
        );

        Line line3 = new Line("3호선", "blue",
            List.of(
                makeSection("신사역", "잠원역", 10),
                makeSection("잠원역", "고속터미널역", 10),
                makeSection("고속터미널역", "서초역", 10))
        );

        Line line9 = new Line("9호선", "yellow",
            List.of(makeSection("고속터미널역", "사평역", 14)));

        Line newLine = new Line("새호선", "black",
            List.of(makeSection("서초역", "사평역", 3))
        );

        lines = List.of(line7, line3, line9, newLine);
    }

    private Section makeSection(String source, String target, int distance) {
        return new Section(new Station(source), new Station(target), distance);
    }

    @DisplayName("한 라인에서 경로를 순방향 조회한다.")
    @Test
    void singleLine() {
        // given
        Station source = new Station("내방역");
        Station target = new Station("논현역");

        // when
        List<Station> stations = new Path(lines, source, target)
            .getStations();

        // then
        assertThat(stations)
            .map(Station::getName)
            .containsExactly("내방역", "고속터미널역", "반포역", "논현역");
    }

    @DisplayName("한 라인에서 경로를 역방향 조회한다.")
    @Test
    void singleLineReverse() {
        // given
        Station source = new Station("논현역");
        Station target = new Station("내방역");

        // when
        List<Station> stations = new Path(lines, source, target)
            .getStations();

        // then
        assertThat(stations)
            .map(Station::getName)
            .containsExactly("논현역", "반포역", "고속터미널역", "내방역");
    }

    @DisplayName("두 라인이 겹쳤을 때 경로를 조회한다.")
    @Test
    void doubleLine() {
        // given
        Station source = new Station("신사역");
        Station target = new Station("논현역");

        // when
        List<Station> stations = new Path(lines, source, target)
            .getStations();

        // then
        assertThat(stations)
            .map(Station::getName)
            .containsExactly("신사역", "잠원역", "고속터미널역", "반포역", "논현역");
    }

    @DisplayName("두 경로 중 짧은 거리를 선택한다.")
    @Test
    void shortestPath() {
        // given
        Station source = new Station("내방역");
        Station target = new Station("사평역");

        // when
        List<Station> stations = new Path(lines, source, target)
            .getStations();

        // then
        assertThat(stations)
            .map(Station::getName)
            .containsExactly("내방역", "고속터미널역", "서초역", "사평역");
    }

    @DisplayName("최단 경로 거리를 반환한다.")
    @Test
    void pathDistance() {
        // given
        Station source = new Station("내방역");
        Station target = new Station("논현역");

        // when
        Path path = new Path(lines, source, target);

        // then
        assertThat(path.getDistance()).isEqualTo(30);
    }

}
