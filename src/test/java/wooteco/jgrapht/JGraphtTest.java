package wooteco.jgrapht;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

public class JGraphtTest {

    @Test
    void searchableGraph() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        List<String> shortestPath = path.getPath("v3", "v1").getVertexList();

        assertThat(path.getPathWeight("v3", "v1")).isEqualTo(4);
        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(shortestPath).containsExactly("v3", "v2", "v1");
    }

    @Test
    void unsearchableGraph() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v3", "v4"), 2);

        DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        assertThat(path.getPath("v1", "v4")).isNull();
        assertThat(path.getPathWeight("v1", "v4")).isNotFinite();
    }
}
