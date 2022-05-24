package study;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class JgraphtTest {

    @Test
    @DisplayName("최단 경로에 포함된 노드 개수를 구할 수 있다.")
    void getDijkstraShortestPath() {
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

    @Test
    @DisplayName("선형일 때도 역방향 최단 거리를 구할 수 있다.")
    void getDijkstraShortestPath_linear() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("최단 경로의 총 거리를 구할 수 있다.")
    void getDijkstraShortestPath_distance() {
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
        GraphPath path = dijkstraShortestPath.getPath("v3", "v1");
        double distance = path.getWeight();

        assertThat(distance).isEqualTo(4);
    }

    @Test
    void test() {
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 잠실역 = new Station(3L, "잠실역");

        WeightedMultigraph<Station, SectionPath> graph = new WeightedMultigraph<>(SectionPath.class);
        graph.addVertex(강남역);
        graph.addVertex(선릉역);
        graph.addVertex(잠실역);

        graph.addEdge(강남역, 선릉역, new SectionPath(1, 5));
        graph.addEdge(선릉역, 잠실역, new SectionPath(2, 10));

        DijkstraShortestPath<Station, SectionPath> stationSectionPathDijkstraShortestPath = new DijkstraShortestPath<>(
                graph);
        Assertions.assertThat(stationSectionPathDijkstraShortestPath.getPathWeight(강남역, 잠실역)).isEqualTo(15);
    }
}
