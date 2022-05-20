package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
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
        Line line = new Line(1L, "name", "color", 100);
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        Station station4 = new Station(3L, "레넌");

        Section section = new Section(1L, line, station1, station2, 3);
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
        Line line1 = new Line(1L, "1호선", "red", 100);
        Line line2 = new Line(2L, "2호선", "green", 200);
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        Station station4 = new Station(4L, "레넌");
        Sections sections = new Sections(
                List.of(
                        new Section(1L, line1, station1, station2, 10),
                        new Section(2L, line1, station2, station3, 5),
                        new Section(3L, line2, station1, station4, 2),
                        new Section(4L, line2, station4, station2, 3),
                        new Section(5L, line2, station2, station3, 10)));
        FindPathStrategy findPathStrategy = new FindDijkstraShortestPathStrategy();

        // when
        Path path = findPathStrategy.findPath(station1, station3, sections);

        // then
        assertThat(path).usingRecursiveComparison()
                .isEqualTo(new Path(List.of(station1, station4, station2, station3), Set.of(line1, line2), 10));
    }

    @Test
    @DisplayName("경로가 없는 경우 예외가 발생한다.")
    void findPathExceptionByNotFoundPath() {
        // given
        Line line = new Line(1L, "name", "color", 100);
        Station station1 = new Station(1L, "오리");
        Station station2 = new Station(2L, "배카라");
        Station station3 = new Station(3L, "오카라");
        Station station4 = new Station(4L, "레넌");
        Sections sections = new Sections(
                List.of(
                        new Section(1L, line, station1, station2, 2),
                        new Section(2L, line, station3, station4, 3)));
        FindPathStrategy findPathStrategy = new FindDijkstraShortestPathStrategy();

        // when & then
        assertThatThrownBy(() -> findPathStrategy.findPath(station1, station4, sections))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("갈 수 있는 경로를 찾을 수 없습니다.");
    }
}
