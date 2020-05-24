package wooteco.subway.admin.acceptance;

import static io.restassured.RestAssured.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {
	static final String STATION_NAME_KANGNAM = "강남역";
	static final String STATION_NAME_YEOKSAM = "역삼역";
	static final String STATION_NAME_SEOLLEUNG = "선릉역";

	static final String LINE_NAME_2 = "2호선";
	static final String LINE_NAME_3 = "3호선";
	static final String LINE_NAME_BUNDANG = "분당선";
	static final String LINE_NAME_SINBUNDANG = "신분당선";

	@LocalServerPort
	int port;

	@Autowired
	ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	ExtractableResponse<Response> get(String path) {
		return given().when().
				get(path).
				then().
				log().all().
				extract();
	}

	<T> T post(String path, Map<String, String> params, Class<T> responseType) {
		return given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(params)
				.when()
				.post(path)
				.then()
				.log().all()
				.statusCode(HttpStatus.CREATED.value())
				.extract().as(responseType);
	}

	void update(String path, Map<String, String> params) {
		given().
				body(params).
				contentType(MediaType.APPLICATION_JSON_VALUE).
				when().
				put(path).
				then().
				log().all().
				statusCode(HttpStatus.OK.value());
	}

	void delete(String path) {
		given()
				.when()
				.delete(path)
				.then()
				.log().all();
	}

	List<StationResponse> getStations() {
		return
				given().when().
						get("/stations").
						then().
						log().all().
						extract().
						jsonPath().getList(".", StationResponse.class);
	}

	StationResponse createStation(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return post(
				"/stations",
				params,
				StationResponse.class
		);
	}

	void deleteStation(Long id) {
		delete("/stations/" + id);
	}

	LineDetailResponse getLine(Long id) {
		return get("/lines/" + id).as(LineDetailResponse.class);
	}

	List<LineResponse> getLines() {
		return get("/lines").jsonPath().getList(".", LineResponse.class);
	}

	LineResponse createLine(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		return post(
				"/lines",
				params,
				LineResponse.class
		);
	}

	void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
		Map<String, String> params = new HashMap<>();
		params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		update(
				"/lines/" + id,
				params
		);
	}

	void deleteLine(Long id) {
		delete("/lines/" + id);
	}

	void addEdge(Long lineId, Long preStationId, Long stationId) {
		addEdge(lineId, preStationId, stationId, 10, 10);
	}

	void addEdge(Long lineId, Long preStationId, Long stationId, Integer distance,
			Integer duration) {
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", preStationId == null ? "" : preStationId.toString());
		params.put("stationId", stationId.toString());
		params.put("distance", distance.toString());
		params.put("duration", duration.toString());

		post(
				"/lines/" + lineId + "/stations",
				params,
				LineResponse.class
		);
	}

	void removeEdge(Long lineId, Long stationId) {
		delete("/lines/" + lineId + "/stations/" + stationId);
	}
}

