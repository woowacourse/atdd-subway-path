package wooteco.subway.admin.acceptance;

import static org.hamcrest.Matchers.*;

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
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
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

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    void createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post("/api/stations").
        then().
            log().all().
            statusCode(HttpStatus.CREATED.value()).
            header("location", notNullValue());
    }

    List<StationResponse> getStations() {
        return
            given().
            when().
                get("/api/stations").
            then().
                log().all().
                extract().
                jsonPath().getList(".", StationResponse.class);
    }

    void deleteStation(Long id) {
        given().
        when().
            delete("/api/stations/" + id).
        then().
            log().all();
    }

    void createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");
        params.put("backgroundColor", "bg-gray-300");

        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post("/api/lines").
        then().
            log().all().
            statusCode(HttpStatus.CREATED.value()).
            header("location", notNullValue());
    }

    LineDetailResponse getLine(Long id) {
        return
            given().
            when().
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
            statusCode(HttpStatus.NO_CONTENT.value());
    }

    List<LineResponse> getLines() {
        return
            given().
            when().
                get("/api/lines").
            then().
                log().all().
                extract().
                jsonPath().getList(".", LineResponse.class);
    }

    List<LineDetailResponse> getLineDetails() {
        return
            given().
            when().
                get("/api/lines/detail").
            then().
                log().all().
                extract().
                jsonPath().
                getList(".", LineDetailResponse.class);
    }

    void deleteLine(Long id) {
        given().
        when().
            delete("/api/lines/" + id).
        then().
            log().all();
    }

    void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
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

    PathResponse findShortestPath(Long source, Long target, String type) {
        return
            given().
                log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                get("/api/paths?source=" + source + "&target=" + target + "&type=" + type).
            then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(PathResponse.class);
    }
}