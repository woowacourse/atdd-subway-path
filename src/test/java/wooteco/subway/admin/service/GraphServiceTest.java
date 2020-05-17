package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;

/**
 *    class description
 *
 *    @author HyungJu An, KuenHwi Choi
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
class GraphServiceTest {
	private Line line1;
	private Line line2;

	@BeforeEach
	void setUp() {
		line1 = Line.of("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5).withId(1L);
		line2 = Line.of("신분당선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5).withId(2L);
	}

	@DisplayName("유효한 출발역과 도착역이 주어지면 적절한 경로를 반환하는지 테스트한다.")
	@Test
	void findPath() {
		line1.addLineStation(new LineStation(null, 1L, 10, 10));
		line1.addLineStation(new LineStation(1L, 2L, 10, 10));
		line1.addLineStation(new LineStation(2L, 3L, 10, 10));

		line2.addLineStation(new LineStation(null, 6L, 10, 10));
		line2.addLineStation(new LineStation(6L, 5L, 10, 10));
		line2.addLineStation(new LineStation(5L, 4L, 10, 10));
		line2.addLineStation(new LineStation(4L, 1L, 10, 10));

		WeightedMultigraph<Long, Edge> graph
			= new WeightedMultigraph(Edge.class);

		List<Line> lines = new ArrayList<>();
		lines.add(line1);
		lines.add(line2);
		PathType type = PathType.DISTANCE;

		lines.stream()
			.flatMap(it -> it.getLineStationsId().stream())
			.forEach(graph::addVertex);

		lines.stream()
			.flatMap(it -> it.getStations().stream())
			.filter(it -> Objects.nonNull(it.getPreStationId()))
			.forEach(it -> {
				Edge edge = Edge.of(it);
				graph.addEdge(it.getPreStationId(), it.getStationId(), edge);
				graph.setEdgeWeight(edge, type.findWeightOf(it));
			});

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		assertThat(dijkstraShortestPath.getPath(6L, 3L).getEdgeList().size()).isEqualTo(5);
	}
}