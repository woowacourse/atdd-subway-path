package wooteco.subway.library.jgrapht;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class JgraphtTest {

    @Test
    void getDijkstraShortestPath() {
        //given
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(
            DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        //when
        List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        //then
        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("Section에 대한 다익스트라 알고리즘 적용, 단일 경로 조회")
    @Test
    void getDijkstraShortestPathAtSection() {
        //given
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(
            DefaultWeightedEdge.class);
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Station 건대입구역 = new Station("건대입구역");
        Station 홍대입구역 = new Station("홍대입구역");
        Section section1 = new Section(강남역, 잠실역, 50);
        Section section2 = new Section(홍대입구역, 강남역, 100);
        Section section3 = new Section(잠실역, 건대입구역, 30);
        graph.addVertex(강남역);
        graph.addVertex(잠실역);
        graph.addVertex(건대입구역);
        graph.addVertex(홍대입구역);
        그래프에_구간을_추가한다(graph, section1);
        그래프에_구간을_추가한다(graph, section2);
        그래프에_구간을_추가한다(graph, section3);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        //when
        List<String> shortestPath = dijkstraShortestPath.getPath(강남역, 건대입구역).getVertexList();
        double weight = dijkstraShortestPath.getPath(강남역, 건대입구역).getWeight();

        //then
        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(weight).isEqualTo(80);
    }

    @DisplayName("Section에 대한 다익스트라 알고리즘 적용, 여러 경로 조회")
    @Test
    void getDijkstraShortestPathsAtSection() {
        //given
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(
            DefaultWeightedEdge.class);
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Station 건대입구역 = new Station("건대입구역");
        Station 홍대입구역 = new Station("홍대입구역");
        Station 논현역 = new Station("논현역");
        Section section1 = new Section(강남역, 잠실역, 50);
        Section section2 = new Section(홍대입구역, 강남역, 100);
        Section section3 = new Section(잠실역, 건대입구역, 30);
        Section section4 = new Section(강남역, 논현역, 10);
        Section section5 = new Section(논현역, 잠실역, 10);
        graph.addVertex(강남역);
        graph.addVertex(잠실역);
        graph.addVertex(건대입구역);
        graph.addVertex(홍대입구역);
        graph.addVertex(논현역);
        그래프에_구간을_추가한다(graph, section1);
        그래프에_구간을_추가한다(graph, section2);
        그래프에_구간을_추가한다(graph, section3);
        그래프에_구간을_추가한다(graph, section4);
        그래프에_구간을_추가한다(graph, section5);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        //when
        List<String> shortestPath = dijkstraShortestPath.getPath(강남역, 잠실역).getVertexList();
        double weight = dijkstraShortestPath.getPath(강남역, 건대입구역).getWeight();

        //then
        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(weight).isEqualTo(50);
    }

    private void 그래프에_구간을_추가한다(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
        Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
            section.getDistance());
    }
}
