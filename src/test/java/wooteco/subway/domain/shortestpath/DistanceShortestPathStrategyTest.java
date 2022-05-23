package wooteco.subway.domain.shortestpath;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

class DistanceShortestPathStrategyTest {

    @Test
    @DisplayName("모든 구간을 파라미터로 받아서 거리에 대한 최단 경로 생성")
    void findShortestPath() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 5, 1L),
                new Section(2L, 1L, 2L, 3L, 5, 2L)
        );
        ShortestPathStrategy shortestPathStrategy = new DistanceShortestPathStrategy();
        List<Long> shortestPath = shortestPathStrategy.findShortestPath(new Sections(sections), 1L, 3L);

        assertThat(shortestPath).containsExactly(1L, 2L, 3L);
    }
}
