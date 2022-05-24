package study;

import static org.assertj.core.api.Assertions.assertThat;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.path.PathEdge;

public class DefaultWeightedEdgeTest {
    private final Fare extraFare = new Fare(1000);

    @Test
    @DisplayName("Custom Edge를 사용해서 원하는 Class type으로 weight를 지정할 수 있다")
    public void dijkstraShortestPath_PathEdge() {
        WeightedMultigraph<String, PathEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        graph.addVertex("마포구청역");
        graph.addVertex("망원역");
        graph.addVertex("합정역");

        PathEdge edgeBetween_마포구청_망원 = new PathEdge(extraFare, 0.15);
        PathEdge edgeBetween_망원_합정 = new PathEdge(extraFare, 0.2);
        graph.addEdge("마포구청역", "망원역", edgeBetween_마포구청_망원);
        graph.addEdge("망원역", "합정역", edgeBetween_망원_합정);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath("마포구청역", "합정역");

        assertThat(path.getWeight()).isEqualTo(0.35);
    }
}
