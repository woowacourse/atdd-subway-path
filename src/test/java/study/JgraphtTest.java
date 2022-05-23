package study;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.ShortestPathEdge;
import wooteco.subway.domain.Station;

public class JgraphtTest {

    @Test
    @DisplayName("최단 경로에 포함된 노드 개수를 구할 수 있다.")
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

    @Test
    @DisplayName("선형일 때도 역방향 최단 거리를 구할 수 있다.")
    public void getDijkstraShortestPath_linear() {
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
    public void getDijkstraShortestPath_distance() {
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
        WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);

        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");
        Station 선릉역 = new Station(3L, "선릉역");
        graph.addVertex(강남역);
        graph.addVertex(역삼역);
        graph.addVertex(선릉역);

        graph.addEdge(강남역, 역삼역, new ShortestPathEdge(1L, 10));
        graph.addEdge(역삼역, 선릉역, new ShortestPathEdge(2L, 20));

        DijkstraShortestPath<Station, ShortestPathEdge> algorithm = new DijkstraShortestPath<>(graph);
        GraphPath<Station, ShortestPathEdge> path = algorithm.getPath(강남역, 선릉역);
        List<ShortestPathEdge> edgeList = path.getEdgeList();

        assertThat(edgeList).hasSize(2);
        assertThat(edgeList.get(0).getLineId()).isEqualTo(1L);
        assertThat(edgeList.get(1).getLineId()).isEqualTo(2L);
        assertThat(path.getWeight()).isEqualTo(30);
    }
}
