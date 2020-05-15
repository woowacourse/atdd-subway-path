package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
	/**
	 * Feature: 지하철 경로 조회
	 * Scenario1: 지하철 경로를 조회한다.
	 * When 지하철 노선 n개 추가 요청을 한다.
	 * Then 지하철 노선이 추가되었다.
	 * <p>
	 * When 지하철 역 n개 추가 요청을 한다.
	 * Then 지하철 역이 추가되었다.
	 * <p>
	 * When 지하철 노선에 구간 추가 요청을 한다.
	 * Then 지하철 노선에 구간이 추가되었다.
	 * <p>
	 * When 출발역과 도착역의 경로 조회 요청을 한다.
	 * Then 최단 거리 기준으로 경로와 기타 정보를 응답한다.
	 * <p>
	 * Scenario2: 잘못된 정보로 지하철 경로를 탐색하면 사용자에게 적절한 응답을 한다.
	 * When 존재하지 않는 출발역 혹은 도착역으로 요청한다.
	 * Then 사용자에게 에러 메세지를 응답한다.
	 * <p>
	 * When 출발역과 도착역이 같은 상태로 요청한다.
	 * Then 사용자에게 에러 메세지를 응답한다.
	 * <p>
	 * When 연결되지 않는 출발역과 도착역으로 요청한다.
	 * Then 사용자에게 에러 메세지를 응답한다.
	 **/

	@Test
	void managePath() {
		/** Scenario1: 지하철 경로를 조회한다. **/
		LineResponse lineResponse1 = createLine(LINE_NAME_2);
		StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
		StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
		StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
		StationResponse stationResponse4 = createStation(STATION_NAME_JAMSIL);
		addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
		addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
		addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());
		addLineStation(lineResponse1.getId(), stationResponse3.getId(), stationResponse4.getId());

		PathResponse pathResponse = searchPath(stationResponse1.getName(), stationResponse4.getName(), "distance");

		assertThat(pathResponse.getStations().size()).isEqualTo(4);
		assertThat(pathResponse.getTotalDuration()).isEqualTo(30);
		assertThat(pathResponse.getTotalDistance()).isEqualTo(30);

		/** Scenario2: 잘못된 정보로 지하철 경로를 탐색하면 사용자에게 적절한 응답을 한다. **/
		String result1 = searchPathWithNotExistStations("삼성역", "교대역", "distance");
		assertThat(result1).contains("저장되지 않은 역을 입력하셨습니다.");

		String result2 = searchPathWithSameStations("삼성역", "삼성역", "distance");
		assertThat(result2).contains("출발역과 도착역은 같을 수 없습니다.");

		LineResponse lineResponse2 = createLine("1호선");
		StationResponse stationResponse5 = createStation("서울역");
		StationResponse stationResponse6 = createStation("용산역");
		StationResponse stationResponse7 = createStation("노량진역");
		StationResponse stationResponse8 = createStation("시청역");
		addLineStation(lineResponse2.getId(), null, stationResponse5.getId());
		addLineStation(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId());
		addLineStation(lineResponse2.getId(), stationResponse6.getId(), stationResponse7.getId());
		addLineStation(lineResponse2.getId(), stationResponse7.getId(), stationResponse8.getId());

		String result3 = searchPathWithUnconnectedStations(stationResponse1.getName(), stationResponse5.getName(),
		                                                   "distance");
		assertThat(result3).contains("출발역과 도착역 간에 경로를 찾을 수 없습니다.");
	}

	private PathResponse searchPath(String source, String target, String type) {
		return given().
				when().
				get("/paths" + "?source=" + source + "&target=" + target + "&type=" + type).
				then().
				log().all().
				statusCode(HttpStatus.OK.value()).
				extract().as(PathResponse.class);
	}

	private String searchPathWithNotExistStations(String source, String target, String type) {
		return given().
				when().
				get("/paths" + "?source=" + source + "&target=" + target + "&type=" + type).
				then().
				log().all().
				statusCode(HttpStatus.NOT_FOUND.value()).
				extract().asString();
	}

	private String searchPathWithSameStations(String source, String target, String type) {
		return given().
				when().
				get("/paths" + "?source=" + source + "&target=" + target + "&type=" + type).
				then().
				log().all().
				statusCode(HttpStatus.BAD_REQUEST.value()).
				extract().asString();
	}

	private String searchPathWithUnconnectedStations(String source, String target, String type) {
		return given().
				when().
				get("/paths" + "?source=" + source + "&target=" + target + "&type=" + type).
				then().
				log().all().
				statusCode(HttpStatus.BAD_REQUEST.value()).
				extract().asString();
	}
}
