package wooteco.subway.routemap.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
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
    private Station 서초, 교대, 강남, 방배;
    private Section 서초_교대, 교대_강남;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        서초 = new Station(1L, "서초역");
        교대 = new Station(2L, "교대역");
        강남 = new Station(3L, "강남역");
        방배 = new Station(4L, "방배역");
        routeMap = new RouteMap(createDummyGraph());
    }

    @DisplayName("노선도에 새로운 역을 추가한다.")
    @Test
    void addStation() {
        // given
        Station 역삼 = new Station(5L, "역삼역");

        // when
        routeMap.addStation(역삼);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(서초, 교대, 강남, 방배, 역삼);
    }

    @DisplayName("노선도에서 역을 제거한다.")
    @Test
    void removeStation() {
        // when
        routeMap.removeStation(방배);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(서초, 교대, 강남);
    }

    @DisplayName("노선도의 역을 업데이트 한다. (역이 제거된 경우)")
    @Test
    void updateStations_removalCase() {
        // given
        Line 강남역_제거된_이호선 = new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Collections.singletonList(서초_교대))
        );
        Set<Station> expectedStations = new Lines(Collections.singletonList(강남역_제거된_이호선))
            .toDistinctStations();

        // when
        routeMap.updateStations(expectedStations);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(서초, 교대);
    }

    @DisplayName("노선도의 역을 업데이트 한다. (역이 추가된 경우)")
    @Test
    void updateStations_addingCase() {
        // given
        Station 역삼 = new Station(4L, "역삼역");
        Section 강남_역삼 = new Section(3L, 강남, 역삼, 2);
        Line addedLine = new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Arrays.asList(서초_교대, 교대_강남, 강남_역삼))
        );
        Set<Station> expectedStations = new Lines(Collections.singletonList(addedLine))
            .toDistinctStations();

        // when
        routeMap.updateStations(expectedStations);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(서초, 교대, 강남, 역삼);
    }

    @DisplayName("노선도의 역을 업데이트 한다. (역의 정보가 수정된 경우)")
    @Test
    void updateStations_valueChangingCase() {
        // given
        Station 변경_서초역 = new Station(서초.getId(), "변경" + 서초.getName());
        Section 변경_서초_교대 = new Section(1L, 변경_서초역, 교대, 5);

        Line updatedLine = new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Arrays.asList(변경_서초_교대, 교대_강남))
        );
        Set<Station> expectedStations = new Lines(Collections.singletonList(updatedLine))
            .toDistinctStations();

        // when
        routeMap.updateStations(expectedStations);

        // then
        assertThat(routeMap.toDistinctStations())
            .containsExactlyInAnyOrder(변경_서초역, 교대, 강남);
    }

    @DisplayName("노선도의 구간을 업데이트 한다.")
    @Test
    void updateSections() {
        // given
        Section 방배_서초 = new Section(3L, 방배, 서초, 2);
        Line updatedLine = new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Arrays.asList(방배_서초, 서초_교대))
        );
        Lines expectedLines = new Lines(Collections.singletonList(updatedLine));

        // when
        routeMap.updateSections(expectedLines);

        // then
        assertThat(routeMap.toPathEdges())
            .containsExactlyInAnyOrder(
                new PathEdge(방배_서초, updatedLine),
                new PathEdge(서초_교대, updatedLine)
            );
    }

    private Multigraph<Station, PathEdge> createDummyGraph() {
        Multigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

        graph.addVertex(서초);
        graph.addVertex(교대);
        graph.addVertex(강남);
        graph.addVertex(방배);

        서초_교대 = new Section(1L, 서초, 교대, 5);
        교대_강남 = new Section(2L, 교대, 강남, 3);

        이호선 = new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Arrays.asList(서초_교대, 교대_강남))
        );

        graph.addEdge(서초, 교대, new PathEdge(서초_교대, 이호선));
        graph.addEdge(교대, 강남, new PathEdge(교대_강남, 이호선));

        return graph;
    }
}
