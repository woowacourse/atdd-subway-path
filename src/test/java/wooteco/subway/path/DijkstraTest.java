package wooteco.subway.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DijkstraTest {

    private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        graph.addVertex(V1);
        graph.addVertex(V2);
        graph.addVertex(V3);
        graph.setEdgeWeight(graph.addEdge(V1, V2), 2);
        graph.setEdgeWeight(graph.addEdge(V2, V3), 2);
        graph.setEdgeWeight(graph.addEdge(V1, V3), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path =
                dijkstraShortestPath.getPath(V3, V1);
        List<String> shortestPath = path.getVertexList();
        double weight = path.getWeight();

        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(weight).isEqualTo(4);
    }
}
