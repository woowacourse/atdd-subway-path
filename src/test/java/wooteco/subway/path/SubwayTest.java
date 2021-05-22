package wooteco.subway.path;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Subway;
import wooteco.subway.path.exception.NoPathException;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("경로 조회 기능")
public class SubwayTest {
    private Subway subway;

    @BeforeEach
    void setUp() {
        Line line1NotConnectedToOthers = createLine1();
        Line line2ConnectedToline3 = createLine2();
        Line line3ConnectedToLine2 = createLine3();
        subway = new Subway(Arrays.asList(
                line1NotConnectedToOthers,
                line2ConnectedToline3,
                line3ConnectedToLine2
        ));
    }

    @DisplayName("이어진 노선들의 최단 경로를 구한다.")
    @Test
    void findShortestPath() {
        GraphPath<Station, DefaultWeightedEdge> graphPath = subway.findPath(
                new Station(4L, "D"),
                new Station(6L, "F")
        );

        assertThat(graphPath.getVertexList()).containsExactly(
                new Station(4L, "D"),
                new Station(7L, "G"),
                new Station(6L, "F")
        );

        assertThat(graphPath.getWeight()).isEqualTo(20);
    }

    @DisplayName("존재하지 않는 최단 경로를 구한다.")
    @Test
    void findNoShortestPath() {
        assertThatThrownBy(() -> subway.findPath(
                new Station(1L, "A"),
                new Station(6L, "F")
        )).isInstanceOf(NoPathException.class);
    }

    private Line createLine3() {
        Station station4 = new Station(4L, "D");
        Station station6 = new Station(6L, "F");
        Station station7 = new Station(7L, "G");

        List<Section> sectionGroup = new ArrayList<>();
        Section section5 = new Section(station4, station7, 10);
        sectionGroup.add(section5);
        Section section6 = new Section(station7, station6, 10);
        sectionGroup.add(section6);
        Sections sections = new Sections(sectionGroup);
        return new Line(3L, "Line3", "Green", sections);
    }

    private Line createLine2() {
        Station station4 = new Station(4L, "D");
        Station station5 = new Station(5L, "E");
        Station station6 = new Station(6L, "F");

        List<Section> sectionGroup = new ArrayList<>();
        Section section3 = new Section(station4, station5, 100);
        sectionGroup.add(section3);
        Section section4 = new Section(station5, station6, 100);
        sectionGroup.add(section4);
        Sections sections = new Sections(sectionGroup);
        return new Line(2L, "Line2", "Red", sections);
    }

    private Line createLine1() {
        Station station1 = new Station(1L, "A");
        Station station2 = new Station(2L, "B");
        Station station3 = new Station(3L, "C");

        List<Section> sectionGroup = new ArrayList<>();
        Section section1 = new Section(station1, station2, 10);
        sectionGroup.add(section1);
        Section section2 = new Section(station2, station3, 10);
        sectionGroup.add(section2);
        Sections sections = new Sections(sectionGroup);
        return new Line(1L, "Line1", "Blue", sections);
    }
}
