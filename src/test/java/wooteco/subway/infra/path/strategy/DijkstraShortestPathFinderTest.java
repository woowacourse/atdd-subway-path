package wooteco.subway.infra.path.strategy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Path;
import wooteco.subway.infra.path.PathFinder;
import wooteco.subway.exception.EmptyResultException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.utils.LineFixture.*;
import static wooteco.subway.utils.SectionFixture.*;
import static wooteco.subway.utils.StationFixture.*;

class DijkstraShortestPathFinderTest {

    private static List<Line> lines;
    private PathFinder pathFinder = new DijkstraShortestPathFinder();

    @BeforeAll
    public static void setUp() {
        Line line1 = createLine1();
        Line line2 = createLine2();
        Line line3 = createLine3();
        Line line4 = createLine4();
        lines = List.of(line1, line2, line3, line4);
    }

    private static Line createLine1() {
        List<Section> sections1 = new ArrayList<>();
        sections1.add(LINE1_SECTION1);
        sections1.add(LINE1_SECTION2);
        sections1.add(LINE1_SECTION3);
        return Line.from(LINE1, sections1);
    }

    private static Line createLine2() {
        List<Section> sections2 = new ArrayList<>();
        sections2.add(LINE2_SECTION1);
        sections2.add(LINE2_SECTION2);
        sections2.add(LINE2_SECTION3);
        return Line.from(LINE2, sections2);
    }

    private static Line createLine3() {
        List<Section> sections3 = new ArrayList<>();
        sections3.add(LINE3_SECTION1);
        sections3.add(LINE3_SECTION2);
        sections3.add(LINE3_SECTION3);
        return Line.from(LINE3, sections3);
    }

    private static Line createLine4() {
        List<Section> sections4 = new ArrayList<>();
        sections4.add(LINE4_SECTION1);
        return Line.from(LINE4, sections4);
    }

    @Test
    @DisplayName("최단경로 거리의 합이 10km 이내인 경우 경로(1,2), 거리(5)가 반환된다.")
    void findShortestPath1() {
        Path path = pathFinder.findShortestPath(STATION1, STATION2, lines);

        assertAll(
                () -> assertThat(path.getStations())
                        .containsExactly(STATION1, STATION2),
                () -> assertThat(path.getDistance()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("최단경로 거리의 합이 10km 이상 50km 이하인 경우 경로(1,2,5,6,7), 거리(20)이 반환된다.")
    void findShortestPath2() {
        Path path = pathFinder.findShortestPath(STATION1, STATION7, lines);

        assertAll(
                () -> assertThat(path.getStations())
                        .containsExactly(STATION1, STATION2, STATION5, STATION6, STATION7),
                () -> assertThat(path.getDistance()).isEqualTo(20)
        );
    }

    @Test
    @DisplayName("최단경로 거리의 합이 50km 초과인 경우 경로(1,2,3,8,9), 거리(58)이 반환되어야 한다.")
    void findShortestPath3() {
        Path path = pathFinder.findShortestPath(STATION1, STATION9, lines);

        assertAll(
                () -> assertThat(path.getStations())
                        .containsExactly(STATION1, STATION2, STATION3, STATION8, STATION9),
                () -> assertThat(path.getDistance()).isEqualTo(58)
        );
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어있지 않으면 예외를 던져야 한다.")
    void findInvalidPath() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(STATION1, STATION10, lines))
                .hasMessage("출발역과 도착역 사이에 연결된 경로가 없습니다.")
                .isInstanceOf(EmptyResultException.class);
    }
}
