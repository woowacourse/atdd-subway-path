package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.admin.acceptance.AcceptanceTest.*;
import static wooteco.subway.admin.acceptance.PageAcceptanceTest.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.admin.domain.CustomEdge;
import wooteco.subway.admin.domain.EdgeWeightType;
import wooteco.subway.admin.domain.LineStation;
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
		List<PathResponse> paths = getPath("신길", "구리");

		PathResponse distanceFirstPath = paths.get(0);
		PathResponse durationFirstPath = paths.get(1);

		//then
		assertThat(distanceFirstPath.getDistance()).isEqualTo(2300);
		assertThat(durationFirstPath.getDuration()).isEqualTo(13);
	}

	public static List<PathResponse> getPath(String source, String target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source);
		params.put("target", target);
		return RestAssured.given().
			params(params).
			when().
			get("/path").
			then().
			log().all().
			extract().jsonPath().getList(".", PathResponse.class);
	}

	@Test
	public void getDijkstraShortestPath() {
		WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);
		LineStation lineStation1 = new LineStation(1L, 2L, 1, 2);
		LineStation lineStation2 = new LineStation(2L, 3L, 4, 8);
		LineStation lineStation3 = new LineStation(3L, 4L, 100, 200);

		CustomEdge customEdge1 = new CustomEdge(lineStation1, EdgeWeightType.DISTANCE);
		CustomEdge customEdge2 = new CustomEdge(lineStation2, EdgeWeightType.DISTANCE);
		CustomEdge customEdge3 = new CustomEdge(lineStation3, EdgeWeightType.DISTANCE);
		graph.addVertex(1L);
		graph.addVertex(2L);
		graph.addVertex(3L);
		graph.addEdge(1L, 2L, customEdge1);
		graph.addEdge(2L, 3L, customEdge2);
		graph.addEdge(1L, 3L, customEdge3);
		graph.setEdgeWeight(customEdge1, customEdge1.getWeight());
		graph.setEdgeWeight(customEdge2, customEdge2.getWeight());
		graph.setEdgeWeight(customEdge3, customEdge3.getWeight());

		DijkstraShortestPath<Long, CustomEdge> dijkstraShortestPath
			= new DijkstraShortestPath<>(graph);
		GraphPath<Long, CustomEdge> path = dijkstraShortestPath.getPath(1L, 3L);
		List<Long> shortestPath = path.getVertexList();

		List<CustomEdge> edgeList = path.getEdgeList();
		double sumDistance = edgeList.stream().mapToDouble(CustomEdge::getDistance).sum();
		double sumDuration = edgeList.stream().mapToDouble(CustomEdge::getDuration).sum();
		assertThat(sumDistance).isEqualTo(5);
		assertThat(sumDuration).isEqualTo(10);

		assertThat(shortestPath.size()).isEqualTo(3);
	}
}
