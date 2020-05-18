package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.PathService;

public class PathAcceptanceTest extends AcceptanceTest {

	private LineResponse lineResponse1;
	private StationResponse 신정역;
	private StationResponse 여의도역;
	private StationResponse 천호역;

	private LineResponse lineResponse2;
	private StationResponse 잠실역;
	private StationResponse 석촌역;

	@DisplayName("지하철 최단경로 조회 Happy Case")
	@Test
	void happyCase1() {
		/*
		 * Given 지하철역이 여러 개 추가되어 있다.
		 * And 지하철 노선이 여러 개 추가되어있다.
		 * And 지하철 노선에 지하철역이 여러 개 추가되어있다.
		 */
		lineResponse1 = createLine("5호선");
		신정역 = createStation("신정역");
		여의도역 = createStation("여의도역");
		천호역 = createStation("천호역");
		addLineStation(lineResponse1.getId(), null, 신정역.getId());
		addLineStation(lineResponse1.getId(), 신정역.getId(), 여의도역.getId());
		addLineStation(lineResponse1.getId(), 여의도역.getId(), 천호역.getId());

		lineResponse2 = createLine("8호선");
		잠실역 = createStation("잠실역");
		석촌역 = createStation("석촌역");
		addLineStation(lineResponse2.getId(), null, 천호역.getId());
		addLineStation(lineResponse2.getId(), 천호역.getId(), 잠실역.getId());
		addLineStation(lineResponse2.getId(), 잠실역.getId(), 석촌역.getId());

		// When : 출발역과 도착역으로 최단경로 조회를 요청한다.
		PathResponse shortestPath = findShortestPath(신정역, 잠실역);

		// Then : 최단경로를 응답 받는다.
		assertThat(shortestPath.getStations().size()).isEqualTo(4);
	}

	@DisplayName("출발역, 도착역 이름이 같은 경우")
	@Test
	void sideCase1() {
		// Given 지하철 5호선 신정역이 추가되어있다.
		lineResponse1 = createLine("5호선");
		신정역 = createStation("신정역");
		addLineStation(lineResponse1.getId(), null, 신정역.getId());

		// When 출발지와 도착지가 같은 역일 때,
		// Then 출발지와 도착지가 같다는 에러를 내려준다.
		assertThat(sendBadRequestForShortestPath(신정역, 신정역).getMessage())
			.isEqualTo(PathService.ERROR_MESSAGE_START_IS_SAME_TO_END);
	}
}
