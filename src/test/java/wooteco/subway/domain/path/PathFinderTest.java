package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.graph.ShortestPathEdge;
import wooteco.subway.domain.graph.SubwayGraph;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.secion.Section;
import wooteco.subway.domain.secion.Sections;

class PathFinderTest {

    private final PathFinder pathFinder = new PathFinder();

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

        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.init(sections);
        GraphPath<Station, ShortestPathEdge> graphResult = subwayGraph.graphResult(
                new Station(1L, "잠실역"), new Station(4L, "건대역"));

        Path path = pathFinder.getPath(graphResult, lines, 20);

        assertAll(() -> assertThat(path.getDistance()).isEqualTo(10.0),
                () -> assertThat(path.getFare()).isEqualTo(1250),
                () -> assertThat(path.getStations().size()).isEqualTo(4));
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

        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.init(sections);
        GraphPath<Station, ShortestPathEdge> graphResult = subwayGraph.graphResult(
                new Station(1L, "잠실역"), new Station(3L, "강남역"));

        Path path = pathFinder.getPath(graphResult, lines, 20);

        assertAll(() -> assertThat(path.getDistance()).isEqualTo(4.0),
                () -> assertThat(path.getFare()).isEqualTo(1250),
                () -> assertThat(path.getStations().size()).isEqualTo(2));
    }

}