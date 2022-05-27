package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.secion.Section;
import wooteco.subway.domain.secion.Sections;
import wooteco.subway.exception.FindPathException;

class PathFinderTest {

    @DisplayName("최대 추가 요금이 0, age가 20일 때, section의 갯수와 상관 없이, 최단 경로를 찾는다.")
    @Test
    void findPathByMaxExtraFare_0_AndAge_20_AndShortestPath() {
        Section section1 = new Section(1L, 1L, new Station(1L, "잠실역"), new Station(2L, "선릉역"), 3);
        Section section2 = new Section(2L, 2L, new Station(2L, "선릉역"), new Station(3L, "강남역"), 3);
        Section section3 = new Section(3L, 3L, new Station(3L, "강남역"), new Station(4L, "건대역"), 4);
        Section section4 = new Section(4L, 4L, new Station(4L, "건대역"), new Station(5L, "사가정역"), 5);
        Section section5 = new Section(4L, 4L, new Station(5L, "사가정역"), new Station(1L, "잠실역"), 7);
        Sections sections = new Sections(List.of(section1, section2, section3, section4, section5));

        Lines lines = new Lines(List.of(new Line(1L, "1호선", "blue", 0),
                new Line(2L, "2호선", "green", 0),
                new Line(3L, "3호선", "orange", 0),
                new Line(4L, "4호선", "black", 0)));

        Station source = new Station(1L, "잠실역");
        Station target = new Station(4L, "건대역");

        PathFinder pathFinder = PathFinder.init(sections, source, target);
        Path path = pathFinder.getPath();

        assertAll(() -> assertThat(path.getDistance()).isEqualTo(10.0),
                () -> assertThat(path.getLineIds()).containsAll(List.of(1L, 2L, 3L)),
                () -> assertThat(path.getStations()).containsAll(List.of(
                        new Station(1L, "잠실역"),
                        new Station(2L, "선릉역"),
                        new Station(3L, "강남역"),
                        new Station(4L, "건대역")
                )));
    }

    @DisplayName("최대 추가 요금이 0, age가 20이고, 구간이 순환할 때, 최단 경로를 찾는다.")
    @Test
    void findPathByMaxExtraFare_0_AndAge_20_AndShortestPathWithRotationPath() {
        Section section1 = new Section(1L, 1L, new Station(1L, "잠실역"), new Station(2L, "선릉역"), 3);
        Section section2 = new Section(2L, 2L, new Station(2L, "선릉역"), new Station(3L, "강남역"), 3);
        Section section3 = new Section(3L, 3L, new Station(3L, "강남역"), new Station(1L, "잠실역"), 4);
        Sections sections = new Sections(List.of(section1, section2, section3));

        Lines lines = new Lines(List.of(new Line(1L, "1호선", "blue", 0),
                new Line(2L, "2호선", "green", 0),
                new Line(3L, "3호선", "orange", 0),
                new Line(4L, "4호선", "black", 0)));

        Station source = new Station(1L, "잠실역");
        Station target = new Station(3L, "강남역");

        PathFinder pathFinder = PathFinder.init(sections, source, target);
        Path path = pathFinder.getPath();

        assertAll(() -> assertThat(path.getDistance()).isEqualTo(4.0),
                () -> assertThat(path.getLineIds()).containsAll(List.of(3L)),
                () -> assertThat(path.getStations()).containsAll(List.of(
                        new Station(1L, "잠실역"),
                        new Station(3L, "강남역")
                )));
    }

    @DisplayName("source 에서 target 이 연결되지 않는다면, 예외를 발생시킨다.")
    @Test
    void notLinkSourceToTargetException() {
        Section section1 = new Section(1L, 1L, new Station(1L, "잠실역"), new Station(2L, "선릉역"), 3);
        Section section2 = new Section(2L, 2L, new Station(2L, "선릉역"), new Station(3L, "강남역"), 3);
        Section section3 = new Section(3L, 3L, new Station(3L, "강남역"), new Station(1L, "건대역"), 4);
        Section section4 = new Section(4L, 4L, new Station(4L, "건대역"), new Station(5L, "사가정역"), 5);
        Sections sections = new Sections(List.of(section1, section2, section3, section4));

        Station source = new Station(1L, "잠실역");
        Station target = new Station(4L, "건대역");

        assertThatThrownBy(() -> PathFinder.init(sections, source, target))
                .isInstanceOf(FindPathException.class)
                .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");
    }


    @DisplayName("source 와 target 이 같다면, 예외를 발생시킨다.")
    @Test
    void sameSourceToTargetException() {
        Section section1 = new Section(1L, 1L, new Station(1L, "잠실역"), new Station(2L, "선릉역"), 3);
        Section section2 = new Section(2L, 2L, new Station(2L, "선릉역"), new Station(3L, "강남역"), 3);
        Section section3 = new Section(3L, 3L, new Station(3L, "강남역"), new Station(1L, "건대역"), 4);
        Section section4 = new Section(4L, 4L, new Station(4L, "건대역"), new Station(5L, "사가정역"), 5);
        Sections sections = new Sections(List.of(section1, section2, section3, section4));

        Station source = new Station(1L, "잠실역");
        Station target = new Station(1L, "잠실역");

        assertThatThrownBy(() -> PathFinder.init(sections, source, target))
                .isInstanceOf(FindPathException.class)
                .hasMessage("출발역과 도착역이 같습니다.");
    }
}
