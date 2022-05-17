package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.exception.SubwayException;

public class PathTest {

    private final List<Section> sections = List.of(
            new Section(1L, 1L, 1L, 2L, 2),
            new Section(2L, 1L, 2L, 3L, 2),
            new Section(3L, 1L, 3L, 4L, 7),
            new Section(4L, 2L, 2L, 5L, 3),
            new Section(5L, 2L, 5L, 4L, 4)
    );
    private final List<Long> stationIds = List.of(1L, 2L, 3L, 4L, 5L);

    @DisplayName("최단 거리 경로를 구한다.")
    @Test
    void findShortestPath() {
        Path path = Path.of(sections, stationIds);

        assertThat(path.findPath(1L, 4L)).containsExactly(1L, 2L, 5L, 4L);
    }

    @DisplayName("최단 거리를 구한다.")
    @Test
    void findShortestDistance() {
        Path path = Path.of(sections, stationIds);

        assertThat(path.findDistance(1L, 4L)).isEqualTo(9);
    }

    @DisplayName("경로가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void notExistPath() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 2),
                new Section(2L, 1L, 2L, 3L, 2),
                new Section(3L, 1L, 3L, 4L, 7),
                new Section(4L, 2L, 5L, 6L, 3)
        );
        List<Long> stationIds = List.of(1L, 2L, 3L, 4L, 5L, 6L);

        Path path = Path.of(sections, stationIds);

        assertThatThrownBy(() -> path.findPath(1L, 5L))
                .isInstanceOf(SubwayException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }
}
