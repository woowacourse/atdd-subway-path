package wooteco.subway.domain.strategy.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.Fixture.강남역;
import static wooteco.subway.Fixture.삼성역;
import static wooteco.subway.Fixture.삼전역;
import static wooteco.subway.Fixture.선릉역;
import static wooteco.subway.Fixture.역삼역;
import static wooteco.subway.Fixture.전체_구간;
import static wooteco.subway.Fixture.종합운동장역;
import static wooteco.subway.Fixture.한티역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.PathNotFoundException;

class PathFindDijkstraStrategyTest {

    @Test
    @DisplayName("각 구간 리스트들을 통해서 최단 경로를 조회할 수 있다.")
    void getDijkstraShortestPath() {
        // given
        PathFindStrategy pathStrategy = new PathFindDijkstraStrategy();

        // when
        Path path = pathStrategy.findPath(강남역, 삼전역, new Sections(전체_구간));

        // then
        assertThat(path).isEqualTo(new Path(List.of(강남역, 역삼역, 선릉역, 삼성역, 종합운동장역, 삼전역), 25));
    }

    @Test
    @DisplayName("시작역과 종점역이 같을 경우 예외가 발생한다.")
    void sameSourceTarget() {
        // given
        PathFindStrategy pathStrategy = new PathFindDijkstraStrategy();

        // when & then
        assertThatThrownBy(() -> pathStrategy.findPath(강남역, 강남역, new Sections(전체_구간)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }

    @Test
    @DisplayName("시작역이 구간에 등록되지 않은 경우 예외가 발생한다.")
    void notIncludedSectionsOfSource() {
        // given
        PathFindStrategy pathStrategy = new PathFindDijkstraStrategy();

        // then
        assertThatThrownBy(() -> pathStrategy.findPath(한티역, 삼전역, new Sections(전체_구간)))
                .isInstanceOf(PathNotFoundException.class);
    }

    @Test
    @DisplayName("시작역이 구간에 등록되지 않은 경우 예외가 발생한다.")
    void notIncludedSectionsOfTarget() {
        // given
        PathFindStrategy pathStrategy = new PathFindDijkstraStrategy();

        // then
        assertThatThrownBy(() -> pathStrategy.findPath(강남역, 한티역, new Sections(전체_구간)))
                .isInstanceOf(PathNotFoundException.class);
    }
}
