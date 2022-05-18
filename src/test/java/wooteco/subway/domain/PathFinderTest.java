package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        Long 노선_1 = 1L;
        Long 노선_2 = 1L;
        Long 노선_3 = 1L;

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(노선_1, 1L, 2L, 50));
        sections.add(new Section(노선_1, 2L, 3L, 8));
        sections.add(new Section(노선_1, 3L, 6L, 20));
        sections.add(new Section(노선_2, 5L, 4L, 10));
        sections.add(new Section(노선_2, 4L, 6L, 5));
        sections.add(new Section(노선_3, 2L, 4L, 6));

        pathFinder = new PathFinder(sections);
    }

    @DisplayName("환승이 없는 경로를 구한다")
    @Test
    void findPathWithoutTransfer() {
        List<Long> path = pathFinder.findPath(5L, 6L);
        assertThat(path).containsExactly(5L, 4L, 6L);
    }

    @DisplayName("환승이 있는 경로를 구한다")
    @Test
    void findPathWithTransfer() {
        List<Long> path = pathFinder.findPath(1L, 6L);
        assertThat(path).containsExactly(1L, 2L, 4L, 6L);
    }

    @DisplayName("환승이 없는 경로의 거리를 구한다")
    @Test
    void findDistanceWithoutTransfer() {
        int distance = pathFinder.findDistance(5L, 6L);
        assertThat(distance).isEqualTo(15);
    }

    @DisplayName("환승이 있는 경로의 거리를 구한다")
    @Test
    void findDistanceWithTransfer() {
        int distance = pathFinder.findDistance(1L, 6L);
        assertThat(distance).isEqualTo(61);
    }
}