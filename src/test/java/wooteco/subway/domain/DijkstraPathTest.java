package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DijkstraPathTest {

    private final Section section1 = new Section(1L, 1L, 1L, 2L, 2);
    private final Section section2 = new Section(2L, 1L, 2L, 3L, 4);
    private final Section section3 = new Section(3L, 1L, 3L, 4L, 6);
    private final Section section4 = new Section(4L, 2L, 3L, 5L, 8);
    private final Section section5 = new Section(4L, 2L, 5L, 6L, 8);

    @DisplayName("출발역id와 도착역id를 받아, 최단 경로에 해당하는 지하철역 id들을 반환한다.")
    @Test
    void getShortestPathStationIds() {
        Path path = new DijkstraPath(List.of(section1, section2, section3, section4));

        List<Long> expected = List.of(1L, 2L, 3L, 5L);

        assertThat(path.getShortestPathStationIds(1L, 5L)).isEqualTo(expected);
    }

    @DisplayName("연결되지 않은 구간의 최단 경로를 조회할 경우 예외가 발생한다.")
    @Test
    void getShortestPathStationIds_NotConnected() {
        Path path = new DijkstraPath(List.of(section1, section2, section3, section5));

        assertThatThrownBy(() -> path.getShortestPathStationIds(1L, 5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }

    @DisplayName("출발역id와 도착역id를 받아, 최단 거리를 반환한다.")
    @Test
    void getShortestPathDistance() {
        Path path = new DijkstraPath(List.of(section1, section2, section3, section4));

        assertThat(path.getShortestPathDistance(1L, 5L)).isEqualTo(14);
    }

    @DisplayName("연결되지 않은 구간의 최단 경로의 거리를 조회할 경우 예외가 발생한다.")
    @Test
    void getShortestPathDistance_NotConnected() {
        Path path = new DijkstraPath(List.of(section1, section2, section3, section5));

        assertThatThrownBy(() -> path.getShortestPathDistance(1L, 5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }

    @DisplayName("최단 경로로 경유하는 노선 id들을 반환한다.")
    @Test
    void getShortestPathLineIds() {
        Path path = new DijkstraPath(List.of(section1, section2, section3, section4));
        List<Long> shortestPathLineIds = path.getShortestPathLineIds(1L, 5L);

        assertThat(shortestPathLineIds).contains(1L, 2L);
    }
}
