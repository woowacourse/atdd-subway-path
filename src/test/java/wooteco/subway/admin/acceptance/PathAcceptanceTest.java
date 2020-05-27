package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class PathAcceptanceTest extends AcceptanceTest {
	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	public void managePath() {
		//given
		LineResponse line1 = createLine("1호선");
		StationResponse station1 = createStation("신길");
		StationResponse station2 = createStation("서울");
		StationResponse station3 = createStation("부평");
		StationResponse station4 = createStation("구리");

		addLineStation(line1.getId(), null, station1.getId(), 1000, 5);
		addLineStation(line1.getId(), station1.getId(), station2.getId(), 500, 3);
		addLineStation(line1.getId(), station2.getId(), station3.getId(), 700, 4);
		addLineStation(line1.getId(), station3.getId(), station4.getId(), 1100, 6);

		//when
		PathResponse pathAsDistance = getPath("신길", "구리", "distance");
		PathResponse pathAsDuration = getPath("신길", "구리", "duration");


		//then
		assertThat(pathAsDistance.getDistance()).isEqualTo(2300);
		assertThat(pathAsDuration.getDuration()).isEqualTo(13);
	}

	public static PathResponse getPath(String source, String target, String type) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source);
		params.put("target", target);
		params.put("type", type);
		return RestAssured.given().
			params(params).
			when().
			get("/path").
			then().
			log().all().
			extract().as(PathResponse.class);
	}
}
