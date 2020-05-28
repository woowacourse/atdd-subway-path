package wooteco.subway.acceptance;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {
	static final String 강남 = "강남역";
	static final String 역삼 = "역삼역";
	static final String 선릉 = "선릉역";

	static final String LINE_NAME_2 = "2호선";
	static final String LINE_NAME_3 = "3호선";
	static final String 분당선 = "분당선";
	static final String 신분당선 = "신분당선";

	@LocalServerPort
	int port;

	public static RequestSpecification given() {
		return RestAssured.given().log().all();
	}

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	protected StationResponse createStation(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return post("/stations", params, StationResponse.class);
	}

	protected List<StationResponse> getStations() {
		return get("/stations", ".", StationResponse.class);
	}

	protected void deleteStation(Long id) {
		delete("/stations/" + id);
	}

	protected LineResponse createLine(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");
		params.put("backgroundColor", "bg-green-200");

		return post("/lines", params, LineResponse.class);
	}

	protected LineDetailResponse getLine(Long id) {
		return get("/lines/" + id, LineDetailResponse.class);
	}

	protected void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
		Map<String, String> params = new HashMap<>();
		params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		put("/lines/" + id, params);
	}

	protected List<LineResponse> getLines() {
		return get("/lines", ".", LineResponse.class);
	}

	protected void deleteLine(Long id) {
		delete("/lines/" + id);
	}

	private void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance,
		Integer duration) {
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", preStationId == null ? "" : preStationId.toString());
		params.put("stationId", stationId.toString());
		params.put("distance", distance.toString());
		params.put("duration", duration.toString());

		post("/lines/" + lineId + "/stations", params);
	}

	protected void addLineStation(Long lineId, Long preStationId, Long stationId) {
		addLineStation(lineId, preStationId, stationId, 10, 10);
	}

	protected void removeLineStation(Long lineId, Long stationId) {
		delete("/lines/" + lineId + "/stations/" + stationId);
	}

	protected <T> T get(String path, Class<T> responseType) {
		return given().when().
			get(path).
			then().
			log().all().
			extract().as(responseType);
	}

	protected <T> List<T> get(String path, String listPath, Class<T> responseType) {
		return given().when().
			get(path).
			then().
			log().all().
			extract().
			jsonPath().
			getList(listPath, responseType);
	}

	protected void post(String path, Map<String, String> params) {
		given().
			body(params).
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			post(path).
			then().
			log().all().
			statusCode(HttpStatus.CREATED.value());
	}

	protected <T> T post(String path, Map<String, String> params,
		Class<T> responseType) {
		return given().
			body(params).
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			post(path).
			then().
			log().all().
			statusCode(HttpStatus.CREATED.value()).
			extract().as(responseType);
	}

	protected void put(String path, Map<String, String> params) {
		given().
			body(params).
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			put(path).
			then().
			log().all().
			statusCode(HttpStatus.OK.value());
	}

	protected void delete(String path) {
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			delete(path).
			then().
			log().all().
			statusCode(HttpStatus.NO_CONTENT.value());
	}

}

