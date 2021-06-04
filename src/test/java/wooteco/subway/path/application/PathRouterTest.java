package wooteco.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathEdge;
import wooteco.subway.path.exception.RoutingFailureException;
import wooteco.subway.station.domain.Station;

class PathRouterTest {

    private PathRouter pathRouter;
    private Station 서초, 교대, 강남, 고터, 노원;
    private Section 서초_교대, 교대_강남, 교대_고터, 고터_강남;
    private Line 이호선, 삼호선;

    @BeforeEach
    void setUp() {
        initializeSubwayData();
        initializePathRouter();
    }

    @DisplayName("역간의 최단 경로를 찾는다.")
    @Test
    void findByShortest() {
        // when
        Path shortestPath = pathRouter.findByShortest(서초, 강남);

        // that
        assertThat(shortestPath.toDistance())
            .isEqualTo(7);

        assertThat(shortestPath.toStations())
            .containsExactly(서초, 교대, 고터, 강남);
    }

    @DisplayName("같은역을 조회할 경우 예외 발생")
    @Test
    void findByShortest_fail_sameStations() {
        assertThatThrownBy(() -> pathRouter.findByShortest(서초, 서초))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("Path 생성에 실패했습니다. (비어있는 간선)");
    }

    @DisplayName("존재하지 않는 경로를 조회한 경우 예외 발생")
    @Test
    void findByShortest_fail_nonexistent() {
        assertThatThrownBy(() -> pathRouter.findByShortest(서초, 노원))
            .isInstanceOf(RoutingFailureException.class)
            .hasMessageContaining("경로 없음");
    }

    private void initializeSubwayData() {
        서초 = new Station(1L, "서초역");
        교대 = new Station(2L, "교대역");
        강남 = new Station(3L, "강남역");
        고터 = new Station(4L, "고속터미널역");
        노원 = new Station(5L, "노원역");

        이호선_생성();
        삼호선_생성();
    }

    private void 이호선_생성() {
        서초_교대 = new Section(1L, 서초, 교대, 5);
        교대_강남 = new Section(2L, 교대, 강남, 3);

        이호선 = new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Arrays.asList(서초_교대, 교대_강남))
        );
    }

    private void 삼호선_생성() {
        교대_고터 = new Section(3L, 교대, 고터, 1);
        고터_강남 = new Section(4L, 고터, 강남, 1);

        삼호선 = new Line(
            2L, "3호선", "orange lighten-1",
            new Sections(Arrays.asList(교대_고터, 고터_강남))
        );
    }

    private void initializePathRouter() {
        Multigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

        graph.addVertex(서초);
        graph.addVertex(교대);
        graph.addVertex(강남);
        graph.addVertex(고터);
        graph.addVertex(노원);

        graph.addEdge(서초, 교대, new PathEdge(서초_교대, 이호선));
        graph.addEdge(교대, 강남, new PathEdge(교대_강남, 이호선));

        graph.addEdge(교대, 고터, new PathEdge(교대_고터, 삼호선));
        graph.addEdge(고터, 강남, new PathEdge(고터_강남, 삼호선));

        pathRouter = new PathRouter(
            new DijkstraShortestPath<>(graph)
        );
    }
}
