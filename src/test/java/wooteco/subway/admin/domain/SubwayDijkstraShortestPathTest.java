package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.exceptions.IllegalPathException;
import wooteco.subway.admin.domain.subwayShortestPath.PathType;
import wooteco.subway.admin.domain.subwayShortestPath.SubwayDijkstraShortestPath;
import wooteco.subway.admin.domain.subwayShortestPath.SubwayShortestPath;

class SubwayDijkstraShortestPathTest extends domainTest {
	Line line1;
	Line line2;
	Line line3;

	Station station1;
	Station station2;
	Station station3;
	Station station4;
	Station station5;
	Station station6;
	Station station7;
	Station station8;
	Station station9;
	Station station10;

	LineStation lineStation1;
	LineStation lineStation2;
	LineStation lineStation3;
	LineStation lineStation4;
	LineStation lineStation5;
	LineStation lineStation6;
	LineStation lineStation7;
	LineStation lineStation8;
	LineStation lineStation9;

	List<Line> lines;
	List<Station> stations;

	@BeforeEach
	void setUp() {
		line1 = createLine("1호선", 1L);
		line2 = createLine("2호선", 2L);
		line3 = createLine("3호선", 3L);

		station1 = createStation("a역", 1L);
		station2 = createStation("b역", 2L);
		station3 = createStation("c역", 3L);
		station4 = createStation("d역", 4L);
		station5 = createStation("e역", 5L);
		station6 = createStation("f역", 6L);
		station7 = createStation("g역", 7L);
		station8 = createStation("h역", 8L);
		station9 = createStation("i역", 9L);
		station10 = createStation("j역", 10L);

		lineStation1 = createLineStation(null, 1L);
		lineStation2 = createLineStation(1L, 2L);
		lineStation3 = createLineStation(2L, 3L);
		lineStation4 = createLineStation(null, 4L);
		lineStation5 = createLineStation(4L, 5L);
		lineStation6 = createLineStation(5L, 6L);
		lineStation7 = createLineStation(null, 6L);
		lineStation8 = createLineStation(6L, 9L);
		lineStation9 = createLineStation(9L, 10L);

		line1.addLineStation(lineStation1);
		line1.addLineStation(lineStation2);
		line1.addLineStation(lineStation3);
		line2.addLineStation(lineStation4);
		line2.addLineStation(lineStation5);
		line2.addLineStation(lineStation6);
		line3.addLineStation(lineStation7);
		line3.addLineStation(lineStation8);
		line3.addLineStation(lineStation9);

		lines = Arrays.asList(line1, line2, line3);
		stations = Arrays.asList(station1, station2, station3, station4, station5,
			station6, station9, station10);
	}

	@DisplayName("출발역에서 도착역으로 가는 최소 거리 경로를 조회한다.")
	@Test
	void findPath_happyCase_ShortestPath() {
		SubwayShortestPath subwayShortestPath = SubwayDijkstraShortestPath
			.of(lines, stations, station4, station10, PathType.DISTANCE);
		assertThat(subwayShortestPath.getVertexList()).isEqualTo(
			Arrays.asList(station4, station5, station6, station9, station10));
		assertThat(subwayShortestPath.getDistance()).isEqualTo(40);
		assertThat(subwayShortestPath.getDuration()).isEqualTo(40);
	}

	@DisplayName("출발역에서 도착역으로 가는 최단 시간 경로를 조회한다.")
	@Test
	void findPath_happyCase_MinimumTimePath() {
		SubwayShortestPath subwayShortestPath = SubwayDijkstraShortestPath
			.of(lines, stations, station4, station10, PathType.DURATION);
		assertThat(subwayShortestPath.getVertexList()).isEqualTo(
			Arrays.asList(station4, station5, station6, station9, station10));
		assertThat(subwayShortestPath.getDistance()).isEqualTo(40);
		assertThat(subwayShortestPath.getDuration()).isEqualTo(40);
	}

	@DisplayName("출발역과 도착역이 같은 경우 IllegalPathException을 던진다.")
	@Test
	void whenSamePath() {
		assertThatThrownBy(() -> {
			SubwayShortestPath subwayShortestPath = SubwayDijkstraShortestPath
				.of(lines, stations, station1, station1, PathType.DISTANCE);
		}).isInstanceOf(IllegalPathException.class);
	}

	@DisplayName("출발역과 도착역이 연결되지 않은 경우 IllegalPathException을 던진다.")
	@Test
	void whenNotConnected() {
		assertThatThrownBy(() -> {
			SubwayShortestPath subwayShortestPath = SubwayDijkstraShortestPath
				.of(lines, stations, station1, station6, PathType.DISTANCE);
		}).isInstanceOf(IllegalPathException.class);
	}

	@DisplayName("출발역과 도착역이 존재하지 않을 경우 IllegalPathException을 던진다.")
	@Test
	void whenNotExistBoth() {
		assertThatThrownBy(() -> {
			SubwayShortestPath subwayShortestPath = SubwayDijkstraShortestPath
				.of(lines, stations, station7, station8, PathType.DISTANCE);
		}).isInstanceOf(IllegalPathException.class);
	}

	@DisplayName("존재하지 않는 출발역을 조회할 경우 IllegalPathException을 던진다.")
	@Test
	void whenNotExistSource() {
		assertThatThrownBy(() -> {
			SubwayShortestPath subwayShortestPath = SubwayDijkstraShortestPath
				.of(lines, stations,
					station7, station1, PathType.DISTANCE);
		}).isInstanceOf(IllegalPathException.class);
	}

	@DisplayName("존재하지 않는 도착역을 조회할 경우 IllegalPathException을 던진다.")
	@Test
	void whenNotExistTarget() {
		assertThatThrownBy(() -> {
			SubwayShortestPath subwayShortestPath = SubwayDijkstraShortestPath
				.of(lines, stations, station1, station7, PathType.DISTANCE);
		}).isInstanceOf(IllegalPathException.class);
	}
}