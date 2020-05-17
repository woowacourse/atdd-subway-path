package wooteco.subway.admin.api;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DijkstraTest {
	@DisplayName("그래프 api 동작 확인 테스트")
	@Test
	public void getDijkstraShortestPath() {
		WeightedMultigraph<String, DefaultWeightedEdge> graph
			= new WeightedMultigraph<>(DefaultWeightedEdge.class);
		graph.addVertex("v1");
		graph.addVertex("v2");
		graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath
			= new DijkstraShortestPath<>(graph);
		List<String> shortestPath
			= dijkstraShortestPath.getPath("v3", "v1").getVertexList();

		assertThat(shortestPath.size()).isEqualTo(3);
	}
}
