package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

class PathTest {

    @DisplayName("경로에서 지나는 호선의 id를 찾는다.")
    @Test
    void findPassedLineIds() {
        Sections sections = new Sections(List.of(
                new Section(new Station("신림역"), new Station("봉천역"), 5, 1L),
                new Section(new Station("봉천역"), new Station("서울대입구역"), 5, 2L),
                new Section(new Station("서울대입구역"), new Station("낙성대역"), 5, 4L),
                new Section(new Station("낙성대역"), new Station("사당역"), 5, 5L)
        ));
        PathFinder pathFinder = new PathFinder();

        Path shortestPath = pathFinder.getShortestPath(new Station("신림역"), new Station("사당역"), sections);
        List<Long> passedLineIds = shortestPath.findPassedLineIds();
        assertThat(passedLineIds).containsExactly(1L, 2L, 4L, 5L);
    }

    @DisplayName("경로에서 지나는 호선의 id를 찾는다.")
    @Test
    void getMaxExtraFare() {
        Line line1 = new Line(1L, "1호선", "blue", 200);
        Line line2 = new Line(2L, "2호선", "yellow", 500);
        Line line3 = new Line(3L, "3호선", "green", 1000);
        Line line4 = new Line(4L, "4호선", "black", 200);

        Sections sections = new Sections(List.of(
                new Section(new Station("신림역"), new Station("봉천역"), 5, 1L),
                new Section(new Station("봉천역"), new Station("서울대입구역"), 5, 2L),
                new Section(new Station("서울대입구역"), new Station("낙성대역"), 5, 3L),
                new Section(new Station("낙성대역"), new Station("사당역"), 5, 4L)
        ));
        PathFinder pathFinder = new PathFinder();

        Path shortestPath = pathFinder.getShortestPath(new Station("신림역"), new Station("사당역"), sections);
        int maxFare = shortestPath.getMaxExtraFare(List.of(line1, line2, line3, line4));

        assertThat(maxFare).isEqualTo(1000);
    }
}
