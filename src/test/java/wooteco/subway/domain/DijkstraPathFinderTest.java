package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DijkstraPathFinderTest {

    private final Section section1 = new Section(1L, 1L, 1L, 2L, 2);
    private final Section section2 = new Section(2L, 1L, 2L, 3L, 4);
    private final Section section3 = new Section(3L, 1L, 3L, 4L, 6);
    private final Section section4 = new Section(4L, 2L, 3L, 5L, 8);
    private final Section section5 = new Section(4L, 2L, 5L, 6L, 8);

    @DisplayName("출발역id와 도착역id를 받아, 최단 경로에 해당하는 지하철역 id들을 반환한다.")
    @Test
    void getShortestPathStationIds() {
        PathFinder pathFinder = new DijkstraPathFinder();
        List<Section> sections = List.of(section1, section2, section3, section4);
        List<Long> expectedStationIds = List.of(1L, 2L, 3L, 5L);
        int expectedDistance = 14;
        List<Long> expectedLineIds = List.of(1L, 2L);

        Path path = pathFinder.getShortestPath(sections, 1L, 5L);

        assertAll(
                () -> assertThat(path.getStationIds()).isEqualTo(expectedStationIds),
                () -> assertThat(path.getDistance()).isEqualTo(expectedDistance),
                () -> assertThat(path.getLineIds()).isEqualTo(expectedLineIds)
        );
    }

    @DisplayName("연결되지 않은 구간의 최단 경로를 조회할 경우 예외가 발생한다.")
    @Test
    void getShortestPathStationIds_NotConnected() {
        PathFinder pathFinder = new DijkstraPathFinder();
        List<Section> invalidSections = List.of(section1, section2, section3, section5);

        assertThatThrownBy(() -> pathFinder.getShortestPath(invalidSections, 1L, 5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }
}
