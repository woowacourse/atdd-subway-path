package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFinder;

class PathFinderTest {

    @DisplayName("두 정점에 대한 최단경로를 가져온다.")
    @Test
    void getShortestPath() {
        // given
        Station 신림역 = new Station("신림역");
        Station 봉천역 = new Station("봉천역");
        Station 서울대입구역 = new Station("서울대입구역");

        Section section1 = new Section(신림역, 봉천역, 10);
        Section section2 = new Section(봉천역, 서울대입구역, 20);
        Section section3 = new Section(신림역, 서울대입구역, 100);

        // when
        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.getShortestPath(서울대입구역, 신림역, new Sections(List.of(section1, section2, section3)));

        // then
        assertAll(
                () -> assertThat(path.getVertexes()).containsExactly(서울대입구역, 봉천역, 신림역),
                () -> assertThat(path.getDistance()).isEqualTo(30)
        );
    }
}
