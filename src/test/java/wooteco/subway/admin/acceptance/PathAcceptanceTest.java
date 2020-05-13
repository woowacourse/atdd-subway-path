package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.admin.acceptance.AcceptanceTest.*;
import static wooteco.subway.admin.acceptance.PageAcceptanceTest.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class PathAcceptanceTest {
	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void managePath() {
		//given
		LineResponse line1 = createLine("1호선");
		StationResponse station1 = createStation("신길");
		StationResponse station2 = createStation("서울");
		StationResponse station3 = createStation("부평");
		StationResponse station4 = createStation("구리");

		addLineStation(line1.getId(), null, station1.getId(), 1000, 5);
		addLineStation(line1.getId(), station1.getId(), station2.getId(), 500, 3);
		addLineStation(line1.getId(), station2.getId(), station3.getId(), 700, 4);
		addLineStation(line1.getId(), station3.getId(), station4.getId(), 1100, 6);

		//when
		PathResponse path = getPath("신길", "구리");

		//then
		assertThat(path.getDistance()).isEqualTo(2300);
		//assertThat(path.getDuration()).isEqualTo(13);
	}

	@Test
	public void getDijkstraShortestPath() {
		WeightedMultigraph<Long, DefaultWeightedEdge> graph
			= new WeightedMultigraph<>(DefaultWeightedEdge.class);
		graph.addVertex(1L);
		graph.addVertex(2L);
		graph.addVertex(3L);
		graph.setEdgeWeight(graph.addEdge(1L, 2L), 2);
		graph.setEdgeWeight(graph.addEdge(2L, 3L), 4);
		graph.setEdgeWeight(graph.addEdge(1L, 3L), 10);
		DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath
			= new DijkstraShortestPath<>(graph);
		List<Long> shortestPath
			= dijkstraShortestPath.getPath(1L, 3L).getVertexList();

		assertThat(shortestPath.size()).isEqualTo(3);
	}

	public static PathResponse getPath(String source, String target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source);
		params.put("target", target);
		return RestAssured.given().
			params(params).
			when().
			get("/path").
			then().
			log().all().
			extract().
			as(PathResponse.class);
	}
}
