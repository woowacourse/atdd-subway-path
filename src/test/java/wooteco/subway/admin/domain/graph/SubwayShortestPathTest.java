package wooteco.subway.admin.domain.graph;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.type.WeightType;

class SubwayShortestPathTest {
	private Graph<Long, SubwayEdge> graph;

	@BeforeEach
	void setUp() {
		LineStation lineStation = new LineStation(1L, 2L, 10, 20);
		LineStation lineStation2 = new LineStation(2L, 3L, 10, 20);
		LineStation lineStation3 = new LineStation(5L, 100L, 10, 20);
		List<LineStation> lineStations = Arrays.asList(lineStation, lineStation2, lineStation3);

		graph = PathFactory.from(lineStations, WeightType.DISTANCE);
	}

	@DisplayName("그래프에 출발지 혹은 도착지가 없는 경우 에러가 발생한다.")
	@Test
	void of() {
		assertThatThrownBy(() -> SubwayShortestPath.of(graph, 1L, 10L))
			.isInstanceOf(PathNotFoundException.class)
			.hasMessage(PathNotFoundException.STATION_NOT_FOUND_MESSAGE);
	}

	@DisplayName("출발지와 목적지사이의 경로가 없는 경우 에러가 발생한다.")
	@Test
	void name() {
		assertThatThrownBy(() -> SubwayShortestPath.of(graph, 1L, 100L))
			.isInstanceOf(PathNotFoundException.class)
			.hasMessage(PathNotFoundException.PATH_NOT_FOUND_MESSAGE);
	}

	@DisplayName("지하철 최단경로의 토탈 거리를 계산한다.")
	@Test
	void calculateTotalDistance() {
		SubwayShortestPath subwayShortestPath = SubwayShortestPath.of(graph, 1L, 3L);
		assertThat(subwayShortestPath.calculateTotalDistance()).isEqualTo(20);
	}

	@DisplayName("지하철 최단경로의 토탈 시간을 계산한다.")
	@Test
	void calculateTotalDuration() {
		SubwayShortestPath subwayShortestPath = SubwayShortestPath.of(graph, 1L, 3L);
		assertThat(subwayShortestPath.calculateTotalDuration()).isEqualTo(40);
	}

	@DisplayName("지하철 최단경로를 역 ID를 순차적으로 가져온다.")
	@Test
	void getShortestPath() {
		SubwayShortestPath subwayShortestPath = SubwayShortestPath.of(graph, 1L, 3L);
		List<Long> shortestPath = subwayShortestPath.getShortestPath();

		assertThat(shortestPath).containsSequence(Arrays.asList(1L, 2L, 3L));
	}
}