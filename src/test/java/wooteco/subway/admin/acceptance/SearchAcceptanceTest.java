package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class SearchAcceptanceTest extends AcceptanceTest {

	@Override
	@BeforeEach
	void setUp() {
		super.setUp();

		LineResponse line1 = createLine(LINE_NAME_2);
		StationResponse kangnam = createStation(STATION_NAME_KANGNAM);
		StationResponse yeoksam = createStation(STATION_NAME_YEOKSAM);
		StationResponse samsung = createStation(STATION_NAME_SAMSUNG);

		addLineStation(line1.getId(), null, kangnam.getId());
		addLineStation(line1.getId(), kangnam.getId(), yeoksam.getId());
		addLineStation(line1.getId(), yeoksam.getId(), samsung.getId());

		LineResponse line2 = createLine(LINE_NAME_SINBUNDANG);
		StationResponse yangjae = createStation(STATION_NAME_YANGJAE);
		StationResponse yangjaeForest = createStation(STATION_NAME_YANGJAE_FOREST);
		addLineStation(line2.getId(), null, kangnam.getId());
		addLineStation(line2.getId(), kangnam.getId(), yangjae.getId());
		addLineStation(line2.getId(), yangjae.getId(),
			yangjaeForest.getId());
	}

	/*
			Feature: 지하철 최단 경로 검색
			Scenario: 지하철 최단 경로를 검색한다.
				Given 지하철역이 여러 개 추가되어있다.
				And 지하철 노선이 여러 개 추가되어있다.
				And 지하철 구간이 여러 개 추가되어있다.

				When 지하철 출발역과 도착역을 입력하고 검색 요청을 한다.
				Then 지하철 최단 거리 경로를 응답 받는다.
				And 지하철 최소 시간 경로를 응답받는다.
		 */
	@DisplayName("지하철 최단 경로를 검색한다.")
	@Test
	void searchPath() {
		List<PathResponse> pathResponses = retrievePaths();

		assertThat(pathResponses.size()).isEqualTo(2);

		PathResponse shortestPath = pathResponses.get(0);
		PathResponse minimumTimePath = pathResponses.get(1);

		assertThat(shortestPath.getStationResponses().size()).isEqualTo(3);
		assertThat(shortestPath.getTotalDistance()).isEqualTo(30);
		assertThat(shortestPath.getTotalDuration()).isEqualTo(30);

		assertThat(minimumTimePath.getStationResponses().size()).isEqualTo(3);
		assertThat(minimumTimePath.getTotalDistance()).isEqualTo(30);
		assertThat(minimumTimePath.getTotalDuration()).isEqualTo(30);
	}
}
