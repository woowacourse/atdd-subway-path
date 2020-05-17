package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DijkstraLearningTest {

    @DisplayName("다익스트라 알고리즘 학습 테스트")
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

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath("v3", "v1");
        List<String> shortestPath = path.getVertexList();
        double weight = path.getWeight();

        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(weight).isEqualTo(4);
    }

    @DisplayName("등록되지 않은 경우")
    @Test
    public void dijkstraShortestPathNotExistException() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        assertThatThrownBy(() -> dijkstraShortestPath.getPath("v3", "v1").getVertexList())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("연결되지 않은 경우")
    @Test
    public void dijkstraShortestPathNotConnectedException() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        assertThatThrownBy(() -> dijkstraShortestPath.getPath("v3", "v1").getVertexList())
            .isInstanceOf(NullPointerException.class);
    }
}
