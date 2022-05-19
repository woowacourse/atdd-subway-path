package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.NotFoundException;

class FindDijkstraShortestPathStrategyTest {

    @Test
    @DisplayName("source와 target이 존재하지 않으면 예외 발생한다.")
    void findPathExceptionByNotExistStations() {
        // given
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        Station station4 = new Station(3L, "레넌");

        Section section = new Section(1L, 1L, station1, station2, 3);
        Sections sections = new Sections(List.of(section));

        FindPathStrategy findPathStrategy = new FindDijkstraShortestPathStrategy();

        // when & then
        assertThatThrownBy(() -> findPathStrategy.findPath(station3, station4, sections))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("최단거리 정보를 반환할 수 있다.")
    void findPath() {
        // given
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        Station station4 = new Station(4L, "레넌");
        Sections sections = new Sections(
                List.of(
                        new Section(1L, 1L, station1, station2, 2),
                        new Section(2L, 1L, station2, station3, 2),
                        new Section(3L, 2L, station1, station4, 3),
                        new Section(4L, 2L, station4, station3, 3)));
        FindPathStrategy findPathStrategy = new FindDijkstraShortestPathStrategy();

        // when
        Path path = findPathStrategy.findPath(station1, station3, sections);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactly(station1, station2, station3),
                () -> assertThat(path.getDistance()).isEqualTo(4)
        );
    }

    @Test
    @DisplayName("경로가 없는 경우 예외가 발생한다.")
    void findPathExceptionByNotFoundPath() {
        // given
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        Station station4 = new Station(4L, "레넌");
        Sections sections = new Sections(
                List.of(
                        new Section(1L, 1L, station1, station2, 2),
                        new Section(2L, 2L, station3, station4, 3)));
        FindPathStrategy findPathStrategy = new FindDijkstraShortestPathStrategy();

        // when & then
        assertThatThrownBy(() -> findPathStrategy.findPath(station1, station4, sections))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("갈 수 있는 경로를 찾을 수 없습니다.");
    }
}
