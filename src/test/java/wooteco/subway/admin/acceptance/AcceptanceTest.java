package wooteco.subway.admin.acceptance;

import static org.hamcrest.Matchers.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
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

        // @formatter:off
        return given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                post("/stations").
            then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().as(StationResponse.class);
        // @formatter:on
    }

    List<StationResponse> getStations() {
        // @formatter:off
        return given().
        when().
            get("/stations").
        then().
            log().all().
            extract().
            jsonPath().getList(".", StationResponse.class);
        // @formatter:on
    }

    void deleteStation(Long id) {
        // @formatter:off
        given().
        when().
            delete("/stations/" + id).
        then().
            log().all();
        // @formatter:on
    }

    LineResponse createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");
        params.put("bgColor", "bg-teal-500");

        // @formatter:off
        return given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post("/lines").
        then().
            log().all().
            statusCode(HttpStatus.CREATED.value()).
            extract().as(LineResponse.class);
        // @formatter:on
    }

    LineDetailResponse getLine(Long id) {
        // @formatter:off
        return given().
        when().
            get("/lines/" + id).
        then().
            log().all().
            extract().as(LineDetailResponse.class);
        // @formatter:on
    }

    void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        // @formatter:off
        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            put("/lines/" + id).
        then().
            log().all().
            statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    List<LineResponse> getLines() {
        // @formatter:off
        return given().
        when().
            get("/lines").
        then().
            log().all().
            extract().
            jsonPath().getList(".", LineResponse.class);
        // @formatter:on
    }

    void deleteLine(Long id) {
        // @formatter:off
        given().
        when().
            delete("/lines/" + id).
        then().
            log().all();
        // @formatter:on
    }

    void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", Objects.isNull(preStationId) ? Strings.EMPTY : preStationId.toString());
        params.put("stationId", stationId.toString());
        params.put("distance", distance.toString());
        params.put("duration", duration.toString());

        // @formatter:off
        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post("/lines/" + lineId + "/stations").
        then().
            log().all().
            statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    void removeLineStation(Long id, Long stationId) {
        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            delete("/lines/" + id + "/stations/" + stationId).
        then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter:on
    }

    WholeSubwayResponse retrieveWholeSubway() {
        // @formatter:off
        return given().
        when().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            get("/lines/detail").
        then().
            log().all().
            statusCode(HttpStatus.OK.value()).
            extract().as(WholeSubwayResponse.class);
        // @formatter:on
    }

    ValidatableResponse retrievePath(String departure, String arrival, PathType type) {
        // @formatter:off
        return given().
            when().
            get("/paths?source=" + departure + "&target=" + arrival + "&type=" + type).
            then().
            log().all().
            statusCode(anyOf(
                is(HttpStatus.OK.value()),
                is(HttpStatus.BAD_REQUEST.value()),
                is(HttpStatus.NOT_FOUND.value())
            ));
        // @formatter:on
    }

    PathResponse getPath(String departure, String arrival, PathType type) {
        // @formatter:off
        return retrievePath(departure, arrival, type)
            .extract().as(PathResponse.class);
        // @formatter:on
    }
}

