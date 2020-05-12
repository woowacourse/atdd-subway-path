package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.*;

import java.time.LocalTime;
import java.util.List;

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
        StationCreateRequest stationCreateRequest = new StationCreateRequest(name);

        return
                given().
                        body(stationCreateRequest).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post("/stations").
                then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(StationResponse.class);
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

    void deleteStation(Long id) {
        given().when().
                delete("/stations/" + id).
        then().
                log().all();
    }

    LineResponse createLine(String name) {
        LineRequest lineRequest = new LineRequest(name, LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        return
                given().
                    body(lineRequest).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                    post("/lines").
                then().
                    log().all().
                    statusCode(HttpStatus.CREATED.value()).
                    extract().as(LineResponse.class);
    }

    LineDetailResponse getLine(Long id) {
        return
                given().when().
                        get("/lines/" + id).
                then().
                        log().all().
                        extract().as(LineDetailResponse.class);
    }

    void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        LineRequest lineRequest = new LineRequest(null, startTime, endTime, 10);

        given().
                body(lineRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                put("/lines/" + id).
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    List<LineResponse> getLines() {
        return
                given().when().
                        get("/lines").
                then().
                        log().all().
                        extract().
                        jsonPath().getList(".", LineResponse.class);
    }

    WholeSubwayResponse getDetailLines() {
        return
                given().when().
                        get("/lines/detail").
                then().
                        log().all().
                        extract().as(WholeSubwayResponse.class);
    }

    void deleteLine(Long id) {
        given().when().
                delete("/lines/" + id).
        then().
                log().all();
    }

    void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        LineStationCreateRequest lineStationCreateRequest = new LineStationCreateRequest(preStationId, stationId, distance, duration);

        given().
                body(lineStationCreateRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines/" + lineId + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    void removeLineStation(Long lineId, Long stationId) {
        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/lines/" + lineId + "/stations/" + stationId).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }
}

