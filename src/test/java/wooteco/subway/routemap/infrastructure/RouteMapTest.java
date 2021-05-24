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
    private Section sectionAB, sectionBC, sectionBD, sectionDC;
    private Line lineA, lineB;

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

        lineA = new Line(
            1L, "lineA", "green lighten-1",
            new Sections(Arrays.asList(sectionAB, sectionBC))
        );

        graph.addEdge(stationA, stationB, new PathEdge(sectionAB, lineA));
        graph.addEdge(stationB, stationC, new PathEdge(sectionBC, lineA));

        // create LineB: B-D-C
        sectionBD = new Section(3L, stationB, stationD, 1);
        sectionDC = new Section(4L, stationD, stationC, 1);

        lineB = new Line(
            2L, "lineB", "black lighten-1",
            new Sections(Arrays.asList(sectionBD, sectionDC))
        );

        graph.addEdge(stationB, stationD, new PathEdge(sectionBD, lineB));
        graph.addEdge(stationD, stationC, new PathEdge(sectionDC, lineB));

        return graph;
    }

    @DisplayName("노선도를 업데이트 한다. (역을 제거하는 경우)")
    @Test
    void updateStations_removalCase() {
        // given
        Lines expectedLines = new Lines(Collections.singletonList(lineA));

        // when
        routeMap.updateStations(expectedLines);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(stationA, stationB, stationC);
    }

    @DisplayName("노선도를 업데이트 한다. (역을 추가하는 경우)")
    @Test
    void updateStations_addingCase() {
        // given
        Station stationE = new Station(5L, "stationE");
        Section sectionCE = new Section(5L, stationC, stationE, 3);
        Line updatedLineB = new Line(
            2L, "lineB", "black lighten-1",
            new Sections(Arrays.asList(sectionBD, sectionDC, sectionCE))
        );
        Lines expectedLines = new Lines(Arrays.asList(lineA, updatedLineB));

        // when
        routeMap.updateStations(expectedLines);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(stationA, stationB, stationC, stationD, stationE);
    }

    @DisplayName("노선도를 업데이트 한다. (역의 정보가 수정되는 경우)")
    @Test
    void updateStations_valueChangingCase() {
        // given
        Station updatedStationA = new Station(stationA.getId(), "updated" + stationA.getName());
        Section updatedSectionAB = new Section(1L, updatedStationA, stationB, 5);

        Line updatedLineA = new Line(
            1L, "lineA", "green lighten-1",
            new Sections(Arrays.asList(updatedSectionAB, sectionBC))
        );
        Lines expectedLines = new Lines(Arrays.asList(updatedLineA, lineB));

        // when
        routeMap.updateStations(expectedLines);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(updatedStationA, stationB, stationC, stationD);
    }
}
