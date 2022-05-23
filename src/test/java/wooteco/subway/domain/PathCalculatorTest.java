package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PathCalculator 는")
class PathCalculatorTest {

    @DisplayName("지하철 최단 경로 목록을 조회한다.")
    @Test
    void findShortestPath() {
        // given
        final List<Section> sections = List.of(
                new Section(1L, 2L, 10),
                new Section(2L, 3L, 10),
                new Section(3L, 4L, 10)
        );
        final PathCalculator pathCalculator = new PathCalculator(sections);

        // when & then
        assertThat(pathCalculator.findShortestPath(1L, 4L)).isEqualTo(List.of(1L, 2L, 3L, 4L));
    }

    @DisplayName("지하철 최단 경로 거리를 구한다.")
    @Test
    void findShortestDistance() {
        // given
        final List<Section> sections = List.of(
                new Section(1L, 2L, 10),
                new Section(2L, 3L, 10),
                new Section(3L, 4L, 10)
        );
        final PathCalculator pathCalculator = new PathCalculator(sections);

        // when & then
        assertThat(pathCalculator.findShortestDistance(1L, 4L))
                .isEqualTo(30);
    }
}
