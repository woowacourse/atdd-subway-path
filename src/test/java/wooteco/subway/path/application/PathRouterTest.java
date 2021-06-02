package wooteco.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathEdge;
import wooteco.subway.station.domain.Station;

class PathRouterTest {

    private PathRouter pathRouter;
    private Station 서초, 교대, 강남, 고터;

    @BeforeEach
    void setUp() {
        서초 = new Station(1L, "서초역");
        교대 = new Station(2L, "교대역");
        강남 = new Station(3L, "강남역");
        고터 = new Station(4L, "고속터미널역");
        pathRouter = new PathRouter(
            new DijkstraShortestPath<>(createDummyGraph())
        );
    }

    @DisplayName("역간의 최단 경로를 찾는다.")
    @Test
    void findByShortest() {
        // when
        Path shortestPath = pathRouter.findByShortest(서초, 강남);

        // that
        assertThat(shortestPath.toDistance())
            .isEqualTo(7);
    }

    private Multigraph<Station, PathEdge> createDummyGraph() {
        Multigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

        graph.addVertex(서초);
        graph.addVertex(교대);
        graph.addVertex(강남);
        graph.addVertex(고터);

        Section 서초_교대 = new Section(1L, 서초, 교대, 5);
        Section 교대_강남 = new Section(2L, 교대, 강남, 3);

        Line 이호선 = new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Arrays.asList(서초_교대, 교대_강남))
        );

        graph.addEdge(서초, 교대, new PathEdge(서초_교대, 이호선));
        graph.addEdge(교대, 강남, new PathEdge(교대_강남, 이호선));

        Section 교대_고터 = new Section(3L, 교대, 고터, 1);
        Section 고터_강남 = new Section(4L, 고터, 강남, 1);

        Line 삼호선 = new Line(
            2L, "3호선", "orange lighten-1",
            new Sections(Arrays.asList(교대_고터, 고터_강남))
        );

        graph.addEdge(교대, 고터, new PathEdge(교대_고터, 삼호선));
        graph.addEdge(고터, 강남, new PathEdge(고터_강남, 삼호선));

        return graph;
    }
}
