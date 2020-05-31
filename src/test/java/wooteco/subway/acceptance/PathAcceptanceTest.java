package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PathAcceptanceTest extends AcceptanceTest {

	@LocalServerPort
	int port;

	/*
	 * Feature : 경로를 조회한다.
	 *
	 * Scenario : 최단 거리를 기준으로 경로를 조회한다.
	 *
	 * Given 지하철 역이 여러 개 추가되어있다.
	 * And 지하철 노선이 여러 개 추가되어있다.
	 * And 지하철 노선에 지하철 역이 여러 개 추가되어있다.
	 *
	 * When 출발역과 도착역을 입력하여 조회 요청을 한다.
	 *
	 * Then 경로에 해당하는 역정보들과 거리, 시간을 응답 받는다.
	 * */

	@DisplayName("최단 거리를 기준으로 경로를 조회")
	@Test
	public void searchPathByShortestDistance() {
		// given
		LineResponse line1 = createLine("1호선");
		StationResponse station1 = createStation("역1");
		StationResponse station2 = createStation("역2");
		StationResponse station3 = createStation("역3");
		StationResponse station4 = createStation("역4");

		LineResponse line2 = createLine("2호선");
		StationResponse station5 = createStation("역5");

		// line 1 : station1 - station2 - station3 - station4
		addLineStation(line1.getId(), null, station1.getId());
		addLineStation(line1.getId(), station1.getId(), station2.getId());
		addLineStation(line1.getId(), station2.getId(), station3.getId());
		addLineStation(line1.getId(), station3.getId(), station4.getId());

		// line2 : station1 - station5 - station4
		addLineStation(line2.getId(), null, station1.getId());
		addLineStation(line2.getId(), station1.getId(), station5.getId());
		addLineStation(line2.getId(), station5.getId(), station4.getId());

		// when
		HashMap<String, String> params = new HashMap<>();
		params.put("source", "역1");
		params.put("target", "역4");
		params.put("type", "DISTANCE");

		PathResponse pathResponses = given()
			.params(params)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/paths")
			.then()
			.statusCode(HttpStatus.OK.value())
			.extract().as(PathResponse.class);

		assertThat(pathResponses.getStations().size()).isEqualTo(3);
	}
}
