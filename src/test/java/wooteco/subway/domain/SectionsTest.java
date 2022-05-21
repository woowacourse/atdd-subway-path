package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findShortestPath() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 10);
        Section section2 = new Section(2L, 1L, 2L, 3L, 20);
        Section section3 = new Section(3L, 1L, 3L,4L, 20);
        Sections sections = new Sections(List.of(section1, section2, section3));

        Path shortestPath = sections.findShortestPath(1L, 4L, 20L);

        assertThat(shortestPath.getDistance()).isEqualTo(50);
        assertThat(shortestPath.calculateFare(List.of(new Line(1L, "2호선", "green", 0)))).isEqualTo(2050);
    }

    @Test
    @DisplayName("최단 경로를 조회한다. - 선택지가 여러 개인 경우 최단 선택")
    void findShortestPathFromVarious() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 10);
        Section section2 = new Section(2L, 1L, 2L, 3L, 20);
        Section section3 = new Section(3L, 1L, 2L, 3L, 15);
        Sections sections = new Sections(List.of(section1, section2, section3));

        Path shortestPath = sections.findShortestPath(1L, 3L, 20L);

        assertThat(shortestPath.getDistance()).isEqualTo(25);
        assertThat(shortestPath.calculateFare(List.of(new Line(1L, "2호선", "green", 0)))).isEqualTo(1550);
    }
}
