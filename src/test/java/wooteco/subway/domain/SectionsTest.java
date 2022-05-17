package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.dto.PathDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findShortestPath() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 10);
        Section section2 = new Section(2L, 1L, 2L, 3L, 20);
        Sections sections = new Sections(List.of(section1, section2));

        PathDto shortestPath = sections.findShortestPath(1L, 3L);

        assertThat(shortestPath.getDistance()).isEqualTo(30);
    }
}
