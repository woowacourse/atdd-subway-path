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
    private Station stationA, stationB, stationC, stationD;

    @BeforeEach
    void setUp() {
        stationA = new Station(1L, "stationA");
        stationB = new Station(2L, "stationB");
        stationC = new Station(3L, "stationC");
        stationD = new Station(4L, "stationD");
        pathRouter = new PathRouter(
            new DijkstraShortestPath<>(createDummyGraph())
        );
    }

    private Multigraph<Station, PathEdge> createDummyGraph() {
        Multigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

        graph.addVertex(stationA);
        graph.addVertex(stationB);
        graph.addVertex(stationC);
        graph.addVertex(stationD);

        // create LineA: A-B-C
        Section sectionAB = new Section(1L, stationA, stationB, 5);
        Section sectionBC = new Section(2L, stationB, stationC, 3);

        Line lineA = new Line(
            1L, "lineA", "green lighten-1",
            new Sections(Arrays.asList(sectionAB, sectionBC))
        );

        graph.addEdge(stationA, stationB, new PathEdge(sectionAB, lineA));
        graph.addEdge(stationB, stationC, new PathEdge(sectionBC, lineA));

        // create LineB: B-D-C
        Section sectionBD = new Section(3L, stationB, stationD, 1);
        Section sectionDC = new Section(4L, stationD, stationC, 1);

        Line lineB = new Line(
            2L, "lineB", "black lighten-1",
            new Sections(Arrays.asList(sectionBD, sectionDC))
        );

        graph.addEdge(stationB, stationD, new PathEdge(sectionBD, lineB));
        graph.addEdge(stationD, stationC, new PathEdge(sectionDC, lineB));

        return graph;
    }

    @DisplayName("역간의 최단 경로를 찾는다.")
    @Test
    void findByShortest() {
        // when
        Path shortestPath = pathRouter.findByShortest(stationA, stationC);

        // that
        assertThat(shortestPath.toDistance())
            .isEqualTo(7);
    }
}
