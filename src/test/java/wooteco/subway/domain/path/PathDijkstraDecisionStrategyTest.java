package wooteco.subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathDijkstraDecisionStrategyTest {

    private final PathDecisionStrategy pathDecisionStrategy = new PathDijkstraDecisionStrategy();

    @DisplayName("지하철 최단 경로 목록과 이동 거리를 조회한다.")
    @Test
    void decidePath() {
        // given
        final List<Section> sections = List.of(
                Section.of(1L, 2L, 10),
                Section.of(2L, 3L, 10),
                Section.of(3L, 4L, 10)
        );
        final PathDecision pathDecision = pathDecisionStrategy.decidePath(sections, Path.of(1L, 4L));

        // when & then
        assertAll(
                () -> assertThat(pathDecision.getStationIds()).isEqualTo(List.of(1L, 2L, 3L, 4L)),
                () -> assertThat(pathDecision.getDistance()).isEqualTo(30)
        );
    }
}
