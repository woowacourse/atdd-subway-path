package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    private static final Section SECTION_1_2 = new Section(1L, 1L, 2L, 10);
    private static final Section SECTION_2_3 = new Section(1L, 2L, 3L, 10);
    private static final Section SECTION_3_4 = new Section(1L, 3L, 4L, 10);
    private static final Section SECTION_3_5 = new Section(2L, 3L, 5L, 8);
    private static final Section SECTION_5_6 = new Section(2L, 5L, 6L, 8);

    @DisplayName("출발역id와 도착역id를 받아, 최단 경로에 해당하는 지하철역 id들을 반환한다.")
    @Test
    void getShortestPathStationIds() {
        final Path path = new Path(new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4, SECTION_3_5)));

        final List<Long> expected = List.of(1L, 2L, 3L, 5L);

        assertThat(path.getShortestPathStationIds(1L, 5L)).isEqualTo(expected);
    }

    @DisplayName("연결되지 않은 구간의 경로를 조회할 경우 예외가 발생한다.")
    @Test
    void getShortestPathStationIds_NotConnected() {
        final Path path = new Path(new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4, SECTION_5_6)));

        assertThatThrownBy(() -> path.getShortestPathStationIds(1L, 6L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }

    @DisplayName("등록되지않은 구간까지의 경로를 조회할 경우 예외가 발생한다.")
    @Test
    void getShortestPathStationIds_NeverRegistered() {
        final Path path = new Path(new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4, SECTION_3_5)));

        assertThatThrownBy(() -> path.getShortestPathStationIds(1L, 6L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 등록 되지 않은 역입니다.");
    }

    @DisplayName("출발역id와 도착역id를 받아, 최단 거리를 반환한다.")
    @Test
    void getShortestPathDistance() {
        final Path path = new Path(new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4, SECTION_3_5)));

        assertThat(path.getShortestPathDistance(1L, 5L)).isEqualTo(28);
    }

    @DisplayName("연결되지 않은 구간의 거리를 조회할 경우 예외가 발생한다.")
    @Test
    void getShortestPathDistance_NotConnected() {
        final Path path = new Path(new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4, SECTION_5_6)));

        assertThatThrownBy(() -> path.getShortestPathDistance(1L, 5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }

    @DisplayName("등록되지않은 구간까지의 거리를 조회할 경우 예외가 발생한다.")
    @Test
    void getShortestPathDistance_NeverRegistered() {
        final Path path = new Path(new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4, SECTION_3_5)));

        assertThatThrownBy(() -> path.getShortestPathDistance(1L, 6L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 등록 되지 않은 역입니다.");
    }
}
