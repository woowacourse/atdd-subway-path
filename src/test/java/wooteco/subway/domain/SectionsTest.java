package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findShortestPath() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 10);
        Section section2 = new Section(2L, 1L, 2L, 3L, 20);
        Section section3 = new Section(3L, 2L, 3L, 4L, 20);
        Sections sections = new Sections(List.of(section1, section2, section3));

        Path shortestPath = sections.findShortestPath(1L, 4L);

        assertAll(
                () -> assertThat(shortestPath.getStationIds()).containsExactly(1L, 2L, 3L, 4L),
                () -> assertThat(shortestPath.getLineIds()).containsExactly(1L, 2L),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(50)
        );
    }

    @Test
    @DisplayName("최단 경로를 조회한다. - 선택지가 여러 개인 경우 최단 선택")
    void findShortestPathFromVarious() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 10);
        Section section2 = new Section(2L, 1L, 2L, 3L, 20);
        Section section3 = new Section(3L, 2L, 2L, 3L, 15);
        Sections sections = new Sections(List.of(section1, section2, section3));

        Path shortestPath = sections.findShortestPath(1L, 3L);

        assertAll(
                () -> assertThat(shortestPath.getStationIds()).containsExactly(1L, 2L, 3L),
                () -> assertThat(shortestPath.getLineIds()).containsExactly(1L, 2L),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(25)
        );
    }
}
