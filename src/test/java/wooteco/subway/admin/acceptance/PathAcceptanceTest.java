package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.ShortestPath;

@Sql("/truncate.sql")
public class PathAcceptanceTest extends AcceptanceTest {

	@DisplayName("최단 경로를 조회")
	@MethodSource("giveCriteriaAndResults")
	@ParameterizedTest
	void getShortestPath(String criteriaName, List<String> stationNames, int expectedDistance, int expectedDuration) {
		//	Feature: 최단 경로를 구한다.
		//		Given 지하철 노선들이 추가되어있다.
		createLine("1호선");
		createLine("2호선");
		createLine("4호선");
		createLine("5호선");
		createLine("9호선");

		//		And 지하철 역들이 추가되어있다.
		createStation("구로");
		createStation("신도림");
		createStation("신길");
		createStation("용산");
		createStation("서울역");
		createStation("충정로");
		createStation("당산");
		createStation("영등포구청");
		createStation("대림");
		createStation("여의도");
		createStation("동작");
		createStation("삼각지");
		createStation("시청");

		//		And	지하철 노선들에 지하철역들이 추가되어있다.
		addLineStation(1L, null, 1L, 10, 10);
		addLineStation(1L, 1L, 2L, 10, 10);
		addLineStation(1L, 2L, 3L, 10, 20);
		addLineStation(1L, 3L, 4L, 20, 20);
		addLineStation(1L, 4L, 5L, 10, 10);
		addLineStation(1L, 5L, 13L, 10, 10);

		addLineStation(2L, null, 13L, 10, 10);
		addLineStation(2L, 13L, 6L, 10, 10);
		addLineStation(2L, 6L, 7L, 10, 50);
		addLineStation(2L, 7L, 8L, 10, 10);
		addLineStation(2L, 8L, 2L, 10, 10);
		addLineStation(2L, 2L, 9L, 10, 10);

		addLineStation(3L, null, 11L, 10, 10);
		addLineStation(3L, 11L, 12L, 10, 10);
		addLineStation(3L, 12L, 5L, 10, 10);

		addLineStation(4L, null, 8L, 10, 10);
		addLineStation(4L, 8L, 3L, 20, 10);
		addLineStation(4L, 3L, 10L, 20, 10);
		addLineStation(4L, 10L, 6L, 20, 10);

		addLineStation(5L, null, 7L, 30, 10);
		addLineStation(5L, 7L, 10L, 30, 10);
		addLineStation(5L, 10L, 11L, 30, 10);

		//		When 시청역부터 신도림역까지 최단 경로를 구하고 싶다.
		ShortestPath shortestPath = findShortestDistancePath("시청", "신도림", criteriaName);

		//		Then 시청역부터 신도림역까지 최단 경로가 구해졌다.
		List<Station> path = shortestPath.getPath();
		int distance = shortestPath.getDistance();
		int duration = shortestPath.getDuration();

		assertEquals(path.size(), 5);
		assertThat(path.get(0).getName()).isEqualTo(stationNames.get(0));
		assertThat(path.get(1).getName()).isEqualTo(stationNames.get(1));
		assertThat(path.get(2).getName()).isEqualTo(stationNames.get(2));
		assertThat(path.get(3).getName()).isEqualTo(stationNames.get(3));
		assertThat(path.get(4).getName()).isEqualTo(stationNames.get(4));

		assertEquals(distance, expectedDistance);
		assertEquals(duration, expectedDuration);
	}

	private static Stream<Arguments> giveCriteriaAndResults() {
		return Stream.of(
			Arguments.of("distance", Arrays.asList("시청", "충정로", "당산", "영등포구청", "신도림"), 40, 80),
			Arguments.of("duration", Arrays.asList("시청", "충정로", "여의도", "신길", "신도림"), 60, 50)
		);
	}
}
