package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @DisplayName("한 노선에서 구간과 역 정보를 통해 최단 경로를 구할 수 있다.")
    @Test
    public void getShortestPath() {
        // given
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(1L, 1L, 4L, 5L, 3));
        sections.add(new Section(2L, 1L, 1L, 2L, 3));
        sections.add(new Section(3L, 1L, 3L, 4L, 4));
        sections.add(new Section(4L, 1L, 2L, 3L, 4));

        final Path path = Path.of(new Sections(sections), 1L, 5L);

        // when
        List<Long> shortestPath = path.getShortestPath();
        final int weight = path.getShortestPathWeight();

        // then
        assertAll(
                () -> assertThat(shortestPath).containsExactly(1L, 2L, 3L, 4L, 5L),
                () -> assertEquals(weight, 14)
        );
    }

    @DisplayName("여러 노선이 존재할 때 구간과 역 정보를 통해 최단 경로를 구할 수 있다.")
    @Test
    public void getShortestPath2() {
        // given
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(1L, 1L, 1L, 2L, 1));
        sections.add(new Section(2L, 1L, 2L, 5L, 2));
        sections.add(new Section(3L, 1L, 5L, 6L, 2));
        sections.add(new Section(4L, 1L, 6L, 7L, 1));

        sections.add(new Section(5L, 2L, 2L, 3L, 1));
        sections.add(new Section(6L, 2L, 3L, 4L, 1));
        sections.add(new Section(7L, 2L, 4L, 6L, 1));

        final Path path = Path.of(new Sections(sections), 1L, 7L);

        // when
        List<Long> shortestPath = path.getShortestPath();
        final int weight = path.getShortestPathWeight();

        // then
        assertAll(
                () -> assertThat(shortestPath).containsExactly(1L, 2L, 3L, 4L, 6L, 7L),
                () -> assertEquals(weight, 5)
        );
    }
}