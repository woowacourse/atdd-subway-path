package wooteco.subway.learning;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LearningTest {
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

    @Test
    @DisplayName("최단경로 패키지 사용 연습")
    void getStationShortestPath() {
        Station 강남 = new Station("강남역");
        Station 선릉 = new Station("선릉역");
        Station 잠실 = new Station("잠실역");
        Station 홍대 = new Station("홍대입구역");

        Section 강남_선릉 = new Section(강남, 선릉, 3);
        Section 강남_잠실 = new Section(강남, 잠실, 3);
        Section 강남_홍대 = new Section(강남, 홍대, 10);
        Section 선릉_홍대 = new Section(선릉, 홍대, 2);
        Section 잠실_홍대 = new Section(강남, 홍대, 5);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        graph.addVertex(강남);
        graph.addVertex(선릉);
        graph.addVertex(잠실);
        graph.addVertex(홍대);
        graph.setEdgeWeight(graph.addEdge(강남, 선릉), 3);
        graph.setEdgeWeight(graph.addEdge(강남, 잠실), 3);
        graph.setEdgeWeight(graph.addEdge(강남, 홍대), 10);
        graph.setEdgeWeight(graph.addEdge(선릉, 홍대), 2);
        graph.setEdgeWeight(graph.addEdge(잠실, 홍대), 5);


        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(강남, 홍대).getVertexList();

        assertThat(shortestPath).contains(강남, 선릉, 홍대);
    }
}
