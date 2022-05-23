package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class ShortestPathTest {

    @Test
    void findShortestDistance() {
        // given
        Line line = new Line(1L, "name", "color", 0);
        Section section1 = new Section(line, 1L, 2L, 3);
        Section section2 = new Section(line, 2L, 3L, 4);
        Section section3 = new Section(line, 3L, 4L, 5);
        Sections sections = new Sections(List.of(section1, section2, section3));
        ShortestPath shortestPath = new ShortestPath(sections);

        // when
        int distance = shortestPath.findShortestDistance(1L, 4L);

        // then
        assertThat(distance).isEqualTo(12);
    }

    @Test
    void findShortestPath() {
        // given
        Line line = new Line(1L, "name", "color", 0);
        Section section1 = new Section(line, 1L, 2L, 3);
        Section section2 = new Section(line, 2L, 3L, 4);
        Section section3 = new Section(line, 3L, 4L, 5);
        Sections sections = new Sections(List.of(section1, section2, section3));
        ShortestPath shortestPath = new ShortestPath(sections);

        // when
        List<Long> stationIds = shortestPath.findShortestPath(4L, 1L);

        // then
        assertThat(stationIds).containsExactlyElementsOf(List.of(4L, 3L, 2L, 1L));
    }
}
