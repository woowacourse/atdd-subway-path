package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

class PathDijkstraAlgorithmTest {

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
                    new Station(3L, "상수역"),
                    new Station(4L, "광흥창역"),
                    new Station(5L, "합정역")
            )
    );

    @DisplayName("최단 거리 경로, 거리, 거쳐간 노선을 구한다.")
    @Test
    void findPath() {
        PathDijkstraAlgorithm algorithm = PathDijkstraAlgorithm.of(sections, stations);
        Path path = algorithm.findPath(1L, 4L);

        assertAll(
                () -> assertThat(path.getStationIds()).containsExactly(1L, 2L, 5L, 4L),
                () -> assertThat(path.getDistance()).isEqualTo(9),
                () -> assertThat(path.getUsedLineIds()).contains(1L, 2L)
        );
    }

    @DisplayName("존재하지 않는 역으로 경로를 찾는 경우 예외가 발생한다.")
    @Test
    void notExistStation() {
        PathDijkstraAlgorithm algorithm = PathDijkstraAlgorithm.of(sections, stations);

        assertThatThrownBy(() -> algorithm.findPath(6L, 7L))
                .isInstanceOf(DataNotExistException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @DisplayName("존재하지 않는 역으로 경로를 찾는 경우 예외가 발생한다.")
    @Test
    void sameSourceAndTarget() {
        PathDijkstraAlgorithm algorithm = PathDijkstraAlgorithm.of(sections, stations);

        assertThatThrownBy(() -> algorithm.findPath(1L, 1L))
                .isInstanceOf(SubwayException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }

    @DisplayName("경로가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void notExistPath() {
        List<Section> sections = List.of(
                new Section(1L, 1L, 1L, 2L, 2),
                new Section(2L, 1L, 2L, 3L, 7),
                new Section(4L, 2L, 4L, 5L, 3)
        );

        PathDijkstraAlgorithm algorithm = PathDijkstraAlgorithm.of(sections, stations);

        assertThatThrownBy(() -> algorithm.findPath(1L, 5L))
                .isInstanceOf(SubwayException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }
}
