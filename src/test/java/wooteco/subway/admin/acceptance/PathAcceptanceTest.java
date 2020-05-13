package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathAcceptanceTest extends AcceptanceTest {

	@DisplayName("최단거리 경로를 조회")
	@Test
	void getShortestPath() {
	//	Feature: 최단거리 경로를 구한다.

	//		Given 지하철 노선에 지하철역이 추가되어있다.
		addLineStation(1L, null, 1L, 10, 10);
		addLineStation(1L, 1L, 2L, 10, 10);
		addLineStation(1L, 2L, 3L, 10, 10);
		addLineStation(1L, 3L, 4L, 20, 20);
		addLineStation(1L, 4L, 5L, 10, 10);
		addLineStation(1L, 5L, 13L, 10, 10);

		addLineStation(2L, null, 13L, 10, 10);
		addLineStation(2L, 13L, 6L, 10, 10);
		addLineStation(2L, 6L, 7L, 10, 10);
		addLineStation(2L, 7L, 8L, 10, 10);
		addLineStation(2L, 8L, 2L, 10, 10);
		addLineStation(2L, 2L, 9L, 10, 10);

		addLineStation(3L, null, 11L, 10, 10);
		addLineStation(3L, 11L, 12L, 10, 10);
		addLineStation(3L, 12L, 5L, 10, 10);

		addLineStation(4L, null, 8L, 10, 10);
		addLineStation(4L, 8L, 3L, 10, 10);
		addLineStation(4L, 3L, 10L, 10, 10);
		addLineStation(4L, 10L, 6L, 10, 10);

		addLineStation(5L, null, 7L, 10, 10);
		addLineStation(5L, 7L, 10L, 10, 10);
		addLineStation(5L, 10L, 11L, 10, 10);

	//		When A역부터 B역까지 최단거리 경로를 구하고 싶다.
		ShortestPath shortestPath = findShortestDistancePath();

	//		Then A역부터 B역까지 최단거리 경로가 구해졌다.
		List<Station> path = shortestPath.getPath();
		int distance = shortestPath.getDistance();
		int duration = shortestPath.getDuration();

		assertThat(path.get(0).getName()).isEqualTo("시청");
		assertThat(path.get(1).getName()).isEqualTo("충정로");
		assertThat(path.get(2).getName()).isEqualTo("당산");
		assertThat(path.get(3).getName()).isEqualTo("영등포구청");
		assertThat(path.get(4).getName()).isEqualTo("신도림");

		assertEquals(distance, 40);
		assertEquals(duration, 40);
	}
}
