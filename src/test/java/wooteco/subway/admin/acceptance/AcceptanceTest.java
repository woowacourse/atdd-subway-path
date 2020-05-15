package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
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
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

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

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	public static RequestSpecification given() {
		return RestAssured.given().log().all();
	}

	StationResponse createStation(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return
			given().
				body(params).
				contentType(MediaType.APPLICATION_JSON_VALUE).
				accept(MediaType.APPLICATION_JSON_VALUE).
				when().
				post("/api/stations").
				then().
				log().all().
				statusCode(HttpStatus.CREATED.value()).
				extract().as(StationResponse.class);
	}

	List<StationResponse> getStations() {
		return
			given().when().
				get("/api/stations").
				then().
				log().all().
				extract().
				jsonPath().getList(".", StationResponse.class);
	}

	void deleteStation(Long id) {
		given().when().
			delete("/api/stations/" + id).
			then().
			log().all();
	}

	LineResponse createLine(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("color", "bg-green-600");
		params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		return
			given().
				body(params).
				contentType(MediaType.APPLICATION_JSON_VALUE).
				accept(MediaType.APPLICATION_JSON_VALUE).
				when().
				post("/api/lines").
				then().
				log().all().
				statusCode(HttpStatus.CREATED.value()).
				extract().as(LineResponse.class);
	}

	LineDetailResponse getLine(Long id) {
		return
			given().when().
				get("/api/lines/" + id).
				then().
				log().all().
				extract().as(LineDetailResponse.class);
	}

	void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
		Map<String, String> params = new HashMap<>();
		params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		given().
			body(params).
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			put("/api/lines/" + id).
			then().
			log().all().
			statusCode(HttpStatus.OK.value());
	}

	List<LineResponse> getLines() {
		return
			given().when().
				get("/api/lines").
				then().
				log().all().
				extract().
				jsonPath().getList(".", LineResponse.class);
	}

	void deleteLine(Long id) {
		given().when().
			delete("/api/lines/" + id).
			then().
			log().all();
	}

	void addLineStation(Long lineId, Long preStationId, Long stationId) {
		if (preStationId == null) {
			addLineStation(lineId, preStationId, stationId, 0, 0);
			return;
		}
		addLineStation(lineId, preStationId, stationId, 10, 10);
	}

	void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance,
		Integer duration) {
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", preStationId == null ? "" : preStationId.toString());
		params.put("stationId", stationId.toString());
		params.put("distance", distance.toString());
		params.put("duration", duration.toString());

		given().
			body(params).
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			post("/api/lines/" + lineId + "/stations").
			then().
			log().all().
			statusCode(HttpStatus.CREATED.value());
	}

	void removeLineStation(Long lineId, Long stationId) {
		given().
			contentType(MediaType.APPLICATION_JSON_VALUE).
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			delete("/api/lines/" + lineId + "/stations/" + stationId).
			then().
			log().all().
			statusCode(HttpStatus.NO_CONTENT.value());
	}

	LineResponse findStationsByLineId(Long lineId) {
		return
			given().
				when().
				get(String.format("/api/lines/%d/stations", lineId)).
				then().
				log().all().
				extract().as(LineResponse.class);
	}

	WholeSubwayResponse retrieveWholeSubway() {
		return given().
			accept(MediaType.APPLICATION_JSON_VALUE).
			when().
			get("/api/lines/detail").
			then().
			log().all().
			statusCode(HttpStatus.OK.value()).
			extract().as(WholeSubwayResponse.class);
	}
}

