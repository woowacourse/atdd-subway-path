package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.Fixture.강남역;
import static wooteco.subway.Fixture.삼성역;
import static wooteco.subway.Fixture.삼전역;
import static wooteco.subway.Fixture.선릉역;
import static wooteco.subway.Fixture.역삼역;
import static wooteco.subway.Fixture.전체_구간;
import static wooteco.subway.Fixture.종합운동장역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    @Test
    @DisplayName("각 구간 리스트들을 통해서 최단 경로를 조회할 수 있다.")
    void getDijkstraShortestPath() {
        // given
        Sections sections = new Sections(전체_구간);

        // when
        PathFinder finder = PathFinder.createPathFinder(sections);
        List<Station> stations = finder.findShortestPath(강남역, 삼전역);

        // then
        assertThat(stations).hasSize(6)
                .containsExactly(강남역, 역삼역, 선릉역, 삼성역, 종합운동장역, 삼전역);
    }

    @Test
    @DisplayName("시작역과 종점역이 같을 경우 예외가 발생한다.")
    void sameSourceTarget() {
        // given
        Sections sections = new Sections(전체_구간);
        PathFinder finder = PathFinder.createPathFinder(sections);

        // when & then
        assertThatThrownBy(() -> finder.findShortestPath(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }

    @Test
    @DisplayName("각 구간 리스트들의 총 거리를 구할 수 있다.")
    void calculatePathDistance() {
        // given
        Sections sections = new Sections(전체_구간);
        PathFinder finder = PathFinder.createPathFinder(sections);

        // when
        double pathDistance = finder.findShortestPathDistance(강남역, 삼전역);

        // then
        assertThat(pathDistance).isEqualTo(25);
    }
}
