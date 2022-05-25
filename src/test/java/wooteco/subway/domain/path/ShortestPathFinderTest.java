package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.exception.SubwayException;

class ShortestPathFinderTest {

    @DisplayName("최단 거리 경로를 구한다.")
    @Test
    void find() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 2),
                new Section(2L, 1L, 2L, 3L, 2),
                new Section(3L, 1L, 3L, 4L, 7),
                new Section(4L, 2L, 2L, 5L, 3),
                new Section(5L, 2L, 5L, 4L, 4)
        );
        List<Long> stationIds = List.of(1L, 2L, 3L, 4L, 5L);

        PathFinder pathFinder = new ShortestPathFinder();
        Path path = pathFinder.find(stationIds, sections, 1L, 4L);

        assertAll(
                () -> {
                    assertThat(path.getDistance()).isEqualTo(9);
                    assertThat(path.getLineIds()).hasSameElementsAs(Set.of(1L, 2L));
                    assertThat(path.getStationIds()).hasSameElementsAs(List.of(1L, 2L, 5L, 4L));
                }
        );
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
        PathFinder pathFinder = new ShortestPathFinder();

        assertThatThrownBy(() -> pathFinder.find(stationIds, sections, 1L, 5L))
                .isInstanceOf(SubwayException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }
}
