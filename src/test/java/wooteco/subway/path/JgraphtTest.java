package wooteco.subway.path;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphtTest {
    private final WeightedGraph<Station, DefaultWeightedEdge> subway
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath map = new DijkstraShortestPath(subway);

    @Test
    void testGetShortestPath() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Station 남부터미널역 = new Station("남부터미널역");

        // 역 추가
        subway.addVertex(강남역);
        subway.addVertex(양재역);
        subway.addVertex(교대역);
        subway.addVertex(남부터미널역);

        // 구간 추가
        DefaultWeightedEdge 신분당선 = subway.addEdge(강남역, 양재역);
        DefaultWeightedEdge 이호선 = subway.addEdge(교대역, 강남역);
        DefaultWeightedEdge 삼호선 = subway.addEdge(교대역, 양재역);
        DefaultWeightedEdge 삼호선추가 = subway.addEdge(교대역, 남부터미널역);

        // 거리 설정
        subway.setEdgeWeight(신분당선, 10);
        subway.setEdgeWeight(이호선, 10);
        subway.setEdgeWeight(삼호선, 5);
        subway.setEdgeWeight(삼호선추가, 3);


        double pathWeight = map.getPathWeight(교대역, 양재역);
        assertThat(pathWeight).isEqualTo(5);
    }
}
