package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.NotFoundPathException;
import wooteco.subway.exception.NotFoundStationException;

class PathTest {

    @Test
    @DisplayName("모든 구간을 파라미터로 받아서 최단 경로 생성")
    void create() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 5, 1L),
                new Section(2L, 1L, 2L, 3L, 5, 2L)
        );

        Path path = Path.of(new Sections(sections), 1L, 3L);

        assertThat(path.getTotalDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("주어진 출발지와 도착지를 현재 구간으로 가지 못할 때 예외 발생")
    void createWithCanNotMakePath() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 5, 1L),
                new Section(2L, 1L, 2L, 3L, 5, 2L),
                new Section(3L, 2L, 4L, 5L, 5, 1L)
        );

        assertThatThrownBy(() -> Path.of(new Sections(sections), 1L, 4L))
                .isInstanceOf(NotFoundPathException.class)
                .hasMessageContaining("현재 구간으로 해당 지하철역을 갈 수 없습니다.");
    }

    @Test
    @DisplayName("주어진 출발지와 도착지 중에 해당 지하철역이 없을 때 예외 발생")
    void createWithWrongStation() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 5, 1L),
                new Section(2L, 1L, 2L, 3L, 5, 2L),
                new Section(3L, 2L, 4L, 5L, 5, 1L)
        );

        assertThatThrownBy(() -> Path.of(new Sections(sections), 1L, 6L))
                .isInstanceOf(NotFoundStationException.class)
                .hasMessageContaining("해당 지하철역이 등록이 안되어 있습니다.");
    }

    @Test
    @DisplayName("주어진 출발지와 도착지가 같은 경우 예외 발생")
    void createWithSameStations() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 5, 1L),
                new Section(2L, 1L, 2L, 3L, 5, 2L),
                new Section(3L, 2L, 4L, 5L, 5, 1L)
        );

        assertThatThrownBy(() -> Path.of(new Sections(sections), 1L, 1L))
                .isInstanceOf(NotFoundPathException.class)
                .hasMessageContaining("같은 위치로는 경로를 찾을 수 없습니다.");
    }
}
