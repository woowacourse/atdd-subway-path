package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

	// Feature: 지하철 최단경로 조회
	// 		Scenario: 지하철의 시작점부터 도착점까지의 최단 경로를 조회한다.
	// 			Given 지하철역이 여러 개 추가되어 있다.
	//     		And 지하철 노선이 여러 개 추가되어있다.
	//     		And 지하철 노선에 지하철역이 여러 개 추가되어있다.
	//
	//			When 출발역과 도착역으로 최단경로 조회를 요청한다.
	//			Then 최단경로를 응답 받는다.

	@DisplayName("지하철 최단경로 조회")
	@Test
	void path() {
		LineResponse lineResponse1 = createLine("5호선");
		StationResponse 신정역 = createStation("신정역");
		StationResponse 여의도역 = createStation("여의도역");
		StationResponse 천호역 = createStation("천호역");
		addLineStation(lineResponse1.getId(), null, 신정역.getId());
		addLineStation(lineResponse1.getId(), 신정역.getId(), 여의도역.getId());
		addLineStation(lineResponse1.getId(), 여의도역.getId(), 천호역.getId());

		LineResponse lineResponse2 = createLine("8호선");
		StationResponse 잠실역 = createStation("잠실역");
		StationResponse 석촌역 = createStation("석촌역");
		addLineStation(lineResponse2.getId(), null, 천호역.getId());
		addLineStation(lineResponse2.getId(), 천호역.getId(), 잠실역.getId());
		addLineStation(lineResponse2.getId(), 잠실역.getId(), 석촌역.getId());

		PathResponse shortestPath = findShortestPath(신정역, 잠실역);

		assertThat(shortestPath.getStationResponses().size()).isEqualTo(4);
	}

}
