package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @DisplayName("연결되지 않은 구간의 경로를 생성할 경우 예외가 발생한다.")
    @Test
    void createPath_NotConnected() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 2);
        Section section2 = new Section(2L, 1L, 2L, 3L, 4);
        Section section3 = new Section(3L, 1L, 3L, 4L, 6);
        Section section4 = new Section(4L, 2L, 5L, 6L, 8);

        assertThatThrownBy(() -> Path.of(List.of(section1, section2, section3, section4), 1L, 5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }

    @DisplayName("연결되지 않은 구간의 경로를 생성할 경우 예외가 발생한다.")
    @Test
    void createPath_NeverRegistered() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 2);
        Section section2 = new Section(2L, 1L, 2L, 3L, 4);
        Section section3 = new Section(3L, 1L, 3L, 4L, 6);
        Section section4 = new Section(4L, 2L, 4L, 5L, 8);

        assertThatThrownBy(() -> Path.of(List.of(section1, section2, section3, section4), 1L, 6L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 등록 되지 않은 역입니다.");
    }

    @DisplayName("출발역id와 도착역id를 받아, 최단 경로에 해당하는 지하철역 id들을 반환한다.")
    @Test
    void getShortestPathStationIds() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 2);
        Section section2 = new Section(2L, 1L, 2L, 3L, 4);
        Section section3 = new Section(3L, 1L, 3L, 4L, 6);
        Section section4 = new Section(4L, 2L, 3L, 5L, 8);

        Path path = Path.of(List.of(section1, section2, section3, section4), 1L, 5L);

        List<Long> expected = List.of(1L, 2L, 3L, 5L);

        assertThat(path.getShortestPathStationIds()).isEqualTo(expected);
    }

    @DisplayName("출발역id와 도착역id를 받아, 최단 거리를 반환한다.")
    @Test
    void getShortestPathDistance() {
        Section section1 = new Section(1L, 1L, 1L, 2L, 2);
        Section section2 = new Section(2L, 1L, 2L, 3L, 4);
        Section section3 = new Section(3L, 1L, 3L, 4L, 6);
        Section section4 = new Section(4L, 2L, 3L, 5L, 8);

        Path path = Path.of(List.of(section1, section2, section3, section4), 1L, 5L);

        assertThat(path.getShortestPathDistance()).isEqualTo(14);
    }
}