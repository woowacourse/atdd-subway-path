package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.SubwayException;

public class PathTest {

    private final List<Section> sections = List.of(
            new Section(1L, 1L, 1L, 2L, 2),
            new Section(2L, 1L, 2L, 3L, 2),
            new Section(3L, 1L, 3L, 4L, 7),
            new Section(4L, 2L, 2L, 5L, 3),
            new Section(5L, 2L, 5L, 4L, 4)
    );
    private final Stations stations = new Stations(
            List.of(
                    new Station(1L, "대흥역"),
                    new Station(2L, "공덕역"),
                    new Station(3L, "공덕역"),
                    new Station(4L, "공덕역"),
                    new Station(5L, "공덕역")
            )
    );

    @DisplayName("최단 거리 경로를 구한다.")
    @Test
    void findShortestPath() {
        Path path = Path.of(1L, 4L, sections, stations);

        assertThat(path.getPath()).containsExactly(1L, 2L, 5L, 4L);
    }

    @DisplayName("최단 거리를 구한다.")
    @Test
    void findShortestDistance() {
        Path path = Path.of(1L, 4L, sections, stations);

        assertThat(path.getDistance()).isEqualTo(9);
    }

    @DisplayName("경로가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void notExistPath() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 2),
                new Section(2L, 1L, 2L, 3L, 7),
                new Section(4L, 2L, 4L, 5L, 3)
        );
        Stations stations = new Stations(
                List.of(
                        new Station(1L, "대흥역"),
                        new Station(2L, "공덕역"),
                        new Station(3L, "공덕역"),
                        new Station(4L, "공덕역"),
                        new Station(5L, "공덕역")
                )
        );

        assertThatThrownBy(() -> Path.of(1L, 5L, sections, stations))
                .isInstanceOf(SubwayException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }
}
