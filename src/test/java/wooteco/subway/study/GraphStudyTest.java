package wooteco.subway.study;

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

public class GraphStudyTest {

    @DisplayName("문자열을 가지는 정점을 이용한 최단경로를 구한다.")
    @Test
    void graphWithString() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path = dijkstraShortestPath.getPath("v3", "v1");
        List<String> shortestPath = path.getVertexList();
        double weight = path.getWeight();
        assertAll(
                () -> assertThat(shortestPath.size()).isEqualTo(3),
                () -> assertThat(weight).isEqualTo(4.0)
        );
    }

    @DisplayName("지하철역을 가지는 정점을 이용한 최단경로를 구한다.")
    @Test
    void graphWithStation() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        Station 신림역 = new Station("신림역");
        Station 봉천역 = new Station("봉천역");
        Station 서울대입구역 = new Station("서울대입구역");
        graph.addVertex(신림역);
        graph.addVertex(봉천역);
        graph.addVertex(서울대입구역);
        graph.setEdgeWeight(graph.addEdge(신림역, 봉천역), 2);
        graph.setEdgeWeight(graph.addEdge(봉천역, 서울대입구역), 2);
        graph.setEdgeWeight(graph.addEdge(신림역, 서울대입구역), 100);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(서울대입구역, 신림역);
        List<Station> shortestPath = path.getVertexList();
        double weight = path.getWeight();
        assertAll(
                () -> assertThat(shortestPath.size()).isEqualTo(3),
                () -> assertThat(shortestPath).containsExactly(서울대입구역, 봉천역, 신림역),
                () -> assertThat(weight).isEqualTo(4.0)
        );
    }

    @DisplayName("custom edge 사용")
    @Test
    void graphWithStation2() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        Station 신림역 = new Station("신림역");
        Station 봉천역 = new Station("봉천역");
        Station 서울대입구역 = new Station("서울대입구역");
        graph.addVertex(신림역);
        graph.addVertex(봉천역);
        graph.addVertex(서울대입구역);
        graph.addEdge(신림역, 봉천역, new MyWeightedEdge("신봉", 2));
        graph.addEdge(봉천역, 서울대입구역, new MyWeightedEdge("봉서", 2));
        graph.addEdge(신림역, 서울대입구역, new MyWeightedEdge("신서", 100));

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(서울대입구역, 신림역);
        List<Station> shortestPath = path.getVertexList();
        double weight = path.getWeight();
        List<DefaultWeightedEdge> edgeList = path.getEdgeList();

        assertAll(
                () -> assertThat(shortestPath.size()).isEqualTo(3),
                () -> assertThat(shortestPath).containsExactly(서울대입구역, 봉천역, 신림역),
                () -> assertThat(weight).isEqualTo(4.0),
                () -> assertThat(edgeList).extracting("name")
                        .contains("봉서", "신봉")
        );
    }
}
