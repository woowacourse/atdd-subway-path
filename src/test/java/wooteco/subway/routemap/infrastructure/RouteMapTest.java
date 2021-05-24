package wooteco.subway.routemap.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.PathEdge;
import wooteco.subway.station.domain.Station;

class RouteMapTest {

    private RouteMap routeMap;
    private Station stationA, stationB, stationC, stationD;
    private Section sectionAB, sectionBC;
    private Line line;

    @BeforeEach
    void setUp() {
        stationA = new Station(1L, "stationA");
        stationB = new Station(2L, "stationB");
        stationC = new Station(3L, "stationC");
        stationD = new Station(4L, "stationD");
        routeMap = new RouteMap(createDummyGraph());
    }

    private Multigraph<Station, PathEdge> createDummyGraph() {
        Multigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

        graph.addVertex(stationA);
        graph.addVertex(stationB);
        graph.addVertex(stationC);
        graph.addVertex(stationD);

        // create LineA: A-B-C
        sectionAB = new Section(1L, stationA, stationB, 5);
        sectionBC = new Section(2L, stationB, stationC, 3);

        line = new Line(
            1L, "line", "green lighten-1",
            new Sections(Arrays.asList(sectionAB, sectionBC))
        );

        graph.addEdge(stationA, stationB, new PathEdge(sectionAB, line));
        graph.addEdge(stationB, stationC, new PathEdge(sectionBC, line));

        return graph;
    }

    @DisplayName("노선도의 역을 업데이트 한다. (역을 제거하는 경우)")
    @Test
    void updateStations_removalCase() {
        // given
        Line lineAfterStationRemoved = new Line(
            1L, "line", "green lighten-1",
            new Sections(Collections.singletonList(sectionAB))
        );
        Lines expectedLines = new Lines(Collections.singletonList(lineAfterStationRemoved));

        // when
        routeMap.updateStations(expectedLines);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(stationA, stationB);
    }

    @DisplayName("노선도의 역을 업데이트 한다. (역을 추가하는 경우)")
    @Test
    void updateStations_addingCase() {
        // given
        Station stationD = new Station(4L, "stationD");
        Section sectionCD = new Section(3L, stationC, stationD, 2);
        Line addedLine = new Line(
            1L, "line", "green lighten-1",
            new Sections(Arrays.asList(sectionAB, sectionBC, sectionCD))
        );
        Lines expectedLines = new Lines(Collections.singletonList(addedLine));

        // when
        routeMap.updateStations(expectedLines);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(stationA, stationB, stationC, stationD);
    }

    @DisplayName("노선도의 역을 업데이트 한다. (역의 정보가 수정되는 경우)")
    @Test
    void updateStations_valueChangingCase() {
        // given
        Station updatedStationA = new Station(stationA.getId(), "updated" + stationA.getName());
        Section updatedSectionAB = new Section(1L, updatedStationA, stationB, 5);

        Line updatedLine = new Line(
            1L, "line", "green lighten-1",
            new Sections(Arrays.asList(updatedSectionAB, sectionBC))
        );
        Lines expectedLines = new Lines(Collections.singletonList(updatedLine));

        // when
        routeMap.updateStations(expectedLines);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(updatedStationA, stationB, stationC);
    }

    @DisplayName("노선도의 구간을 업데이트 한다.")
    @Test
    void updateSections() {
        // given
        Section sectionDA = new Section(3L, stationD, stationA, 2);
        Line updatedLine = new Line(
            1L, "line", "green lighten-1",
            new Sections(Arrays.asList(sectionDA, sectionAB))
        );
        Lines expectedLines = new Lines(Collections.singletonList(updatedLine));

        // when
        routeMap.updateSections(expectedLines);

        // then
        assertThat(routeMap.toPathEdges())
            .containsExactlyInAnyOrder(
                new PathEdge(sectionDA, updatedLine),
                new PathEdge(sectionAB, updatedLine)
            );
    }
}
