package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.domain.path.PathFixture.강남역;
import static wooteco.subway.domain.path.PathFixture.분당선;
import static wooteco.subway.domain.path.PathFixture.신림역;
import static wooteco.subway.domain.path.PathFixture.신분당선;
import static wooteco.subway.domain.path.PathFixture.신촌역;
import static wooteco.subway.domain.path.PathFixture.이호선;
import static wooteco.subway.domain.path.PathFixture.잠실역;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Lines;

class DijkstraStrategyTest {

    @Test
    @DisplayName("전체 노선과 출발역과 도착역을 받아 최단 경로를 반환한다.")
    void findPath() {
        Path path = new DijkstraStrategy().findPath(List.of(신분당선, 분당선), 신촌역, 잠실역);
        Path expected = new Path(Arrays.asList(신촌역, 강남역, 잠실역), 10, new Lines(Arrays.asList(분당선)));

        assertThat(path)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("해당 경로가 존재하지 않으면 예외가 발생한다.")
    void validatePath() {
        assertThatThrownBy(() -> new DijkstraStrategy().findPath(List.of(신분당선, 분당선, 이호선), 강남역, 신림역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 경로가 존재하지 않습니다.");
    }
}
