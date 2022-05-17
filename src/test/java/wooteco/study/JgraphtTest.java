package wooteco.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Station;

public class JgraphtTest {

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("지하철역을 기반으로 최단 경로를 확인한다.")
    @Test
    void getDijkstraShortestPathWithStation() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        Station 낙성대역 = new Station("낙성대역");
        Station 신림역 = new Station("신림역");
        Station 신대방역 = new Station("신대방역");
        Station 신도림역 = new Station("신도림역");
        graph.addVertex(낙성대역);
        graph.addVertex(신림역);
        graph.addVertex(신대방역);
        graph.addVertex(신도림역);
        graph.setEdgeWeight(graph.addEdge(낙성대역, 신림역), 10);
        graph.setEdgeWeight(graph.addEdge(신림역, 신대방역), 10);
        graph.setEdgeWeight(graph.addEdge(신대방역, 신도림역), 10);
        graph.setEdgeWeight(graph.addEdge(낙성대역, 신도림역), 50);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(낙성대역, 신도림역);
        List<Station> vertexes = shortestPath.getVertexList();

        assertAll(
                () -> assertThat(vertexes.size()).isEqualTo(4),
                () -> assertThat(shortestPath.getWeight()).isEqualTo(30)
        );
    }
}
