package wooteco.subway;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DijkstraTest {

    @Test
    @DisplayName("경로 테스트")
    void findPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("a");
        graph.addVertex("b");
        graph.addVertex("c");
        graph.setEdgeWeight(graph.addEdge("a", "b"), 4);
        graph.setEdgeWeight(graph.addEdge("a", "b"), 2);
        graph.setEdgeWeight(graph.addEdge("a", "b"), 1);
        graph.setEdgeWeight(graph.addEdge("b", "c"), 1);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("a", "c").getVertexList();

        assertThat(dijkstraShortestPath.getPath("a", "c").getWeight()).isEqualTo(2);
    }
}
