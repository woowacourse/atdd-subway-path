package study;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

public class JgraphtTest {

    // https://jgrapht.org/guide/UserOverview#graph-algorithms
    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        List<String> path = shortestPath.getPath("v3", "v1").getVertexList();
        double distance = shortestPath.getPathWeight("v3", "v1");

        assertThat(path).containsExactly("v3", "v2", "v1");
        assertThat(distance).isEqualTo(4);
    }

    @Test
    public void customEdgeShortestPath() {
        WeightedMultigraph<String, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");

        CustomEdge edge1 = new CustomEdge(1L, "v1", "v2", 2);
        CustomEdge edge2 = new CustomEdge(2L, "v2", "v3", 2);
        CustomEdge edge3 = new CustomEdge(1L, "v1", "v3", 100);
        graph.addEdge("v1", "v2", edge1);
        graph.addEdge("v2", "v3", edge2);
        graph.addEdge("v1", "v3", edge3);

        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        GraphPath<String, CustomEdge> path = shortestPath.getPath("v3", "v1");
        List<String> vertexInOrder = path.getVertexList();
        List<CustomEdge> edgesInOrder = path.getEdgeList();
        double distance = path.getWeight();

        assertThat(vertexInOrder).containsExactly("v3", "v2", "v1");
        assertThat(edgesInOrder).containsExactly(edge2, edge1);
        assertThat(distance).isEqualTo(4);
    }

    private static class CustomEdge extends DefaultWeightedEdge {

        final Long lineId;
        final String source;
        final String target;
        final int distance;

        private CustomEdge(Long lineId, String source, String target, int distance) {
            this.lineId = lineId;
            this.source = source;
            this.target = target;
            this.distance = distance;
        }

        @Override // setEdgeWeight 생략 가능
        public double getWeight() {
            return distance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CustomEdge section = (CustomEdge) o;
            return distance == section.distance
                    && Objects.equals(lineId, section.lineId)
                    && Objects.equals(source, section.source)
                    && Objects.equals(target, section.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(lineId, source, target, distance);
        }
    }
}
