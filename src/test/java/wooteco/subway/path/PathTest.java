package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

import java.util.Arrays;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

public class PathTest extends AcceptanceTest {

    private LineResponse expectedShortestPath;
    private LineResponse expectedNoShortestPath;
    private StationResponse sourceStation1;
    private StationResponse shortestStation2;
    private StationResponse noShortestStation1;
    private StationResponse shortestStation3;
    private StationResponse shortestStation4;
    private StationResponse destinationStation1;

    @Autowired
    private PathService pathService;

    /**
     * 교대역    --- *2호선* ---   강남역 | | *3호선*                   *신분당선* | | 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        sourceStation1 = 지하철역_등록되어_있음("sourceStation1");
        destinationStation1 = 지하철역_등록되어_있음("destinationStation1");
        shortestStation2 = 지하철역_등록되어_있음("shortestStation2");
        shortestStation3 = 지하철역_등록되어_있음("shortestStation3");
        shortestStation4 = 지하철역_등록되어_있음("shortestStation4");
        noShortestStation1 = 지하철역_등록되어_있음("noShortestStation1");

        expectedShortestPath = 지하철_노선_등록되어_있음("expectedShortestPath", "bg-red-600",
            shortestStation2, shortestStation3, 10);
        expectedNoShortestPath = 지하철_노선_등록되어_있음("expectedNoShortestPath", "bg-red-600",
            sourceStation1, destinationStation1, 100);

        지하철_구간_등록되어_있음(expectedShortestPath, sourceStation1, shortestStation2, 10);
        지하철_구간_등록되어_있음(expectedShortestPath, shortestStation3, shortestStation4, 10);
        지하철_구간_등록되어_있음(expectedShortestPath, shortestStation4, destinationStation1, 10);
    }


    @DisplayName("최단경로 알고리즘 기본 입출력 테스트")
    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
        List<String> shortestPath
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("최단경로 알고리즘 경로 출력 테스트")
    @Test
    public void getDijkstraShortestPathStations() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
        List<String> shortestPath
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(shortestPath).isEqualTo(Arrays.asList("v3", "v2", "v1"));
    }

    @DisplayName("최단경로 탐색")
    @Test
    public void findShortestDistance() {

        PathResponse path = pathService.findPath(1L, 2L);

        assertThat(path.getStations()).hasSize(5);
        assertThat(path.getStations()).isEqualTo(Arrays.asList(
            sourceStation1,
            shortestStation2,
            shortestStation3,
            shortestStation4,
            destinationStation1));
        assertThat(path.getDistance()).isEqualTo(40);
    }
}
