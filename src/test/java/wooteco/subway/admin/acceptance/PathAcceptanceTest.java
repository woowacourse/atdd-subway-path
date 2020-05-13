package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
	/** Feature: 지하철 경로 조회
	        Scenario: 지하철 경로를 조회한다.
	            When 지하철 노선 n개 추가 요청을 한다.
	            Then 지하철 노선이 추가되었다.

	            When 지하철 역 n개 추가 요청을 한다.
	            Then 지하철 역이 추가되었다.

	            When 지하철 노선에 구간 추가 요청을 한다.
	            Then 지하철 노선에 구간이 추가되었다.

	            When 출발역과 도착역의 경로 조회 요청을 한다.
	            Then 최단 거리 기준으로 경로와 기타 정보를 응답한다.
	 **/

	@Test
	void managePath() {
		LineResponse lineResponse1 = createLine(LINE_NAME_2);
		StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
		StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
		StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
		StationResponse stationResponse4 = createStation(STATION_NAME_JAMSIL);
		addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
		addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
		addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());
		addLineStation(lineResponse1.getId(), stationResponse3.getId(), stationResponse4.getId());

		PathResponse pathResponse = searchPath(stationResponse1.getName(), stationResponse4.getName());

		assertThat(pathResponse.getStations().size()).isEqualTo(4);
		assertThat(pathResponse.getTotalDuration()).isEqualTo(30);
		assertThat(pathResponse.getTotalDistance()).isEqualTo(30);
	}

	private PathResponse searchPath(String source, String target) {
		return given().
				when().
					get("/path" + "?source=" + source + "&target=" + target).
				then().
					log().all().
					statusCode(HttpStatus.OK.value()).
					extract().as(PathResponse.class);
	}
}
