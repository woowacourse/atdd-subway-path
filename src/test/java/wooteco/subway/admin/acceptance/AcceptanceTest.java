package wooteco.subway.admin.acceptance;

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
import io.restassured.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.StandardResponse;
import wooteco.subway.admin.dto.response.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {
    static final String STATION_NAME_KANGNAM = "강남역";
    static final String STATION_NAME_YEOKSAM = "역삼역";
    static final String STATION_NAME_SEOLLEUNG = "선릉역";
    static final String STATION_NAME_JAMSIL = "잠실역";
    static final String STATION_NAME_GYODAE = "교대역";
    static final String STATION_NAME_JAMWON = "잠원역";
    static final String STATION_NAME_SINSA = "신사역";

    static final String LINE_NAME_2 = "2호선";
    static final String LINE_NAME_3 = "3호선";
    static final String LINE_NAME_BUNDANG = "분당선";
    static final String LINE_NAME_SINBUNDANG = "신분당선";

    @LocalServerPort
    int port;

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    StationResponse createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        StandardResponse<StationResponse> response = given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            post("/stations").
            then().
            log().all().
            statusCode(HttpStatus.CREATED.value()).
            extract().as(new TypeRef<StandardResponse<StationResponse>>() {
        });
        return response.getData();
    }

    List<StationResponse> getStations() {
        StandardResponse<List<StationResponse>> response = given().when().
            get("/stations").
            then().
            log().all().
            extract().as(new TypeRef<StandardResponse<List<StationResponse>>>() {
        });
        return response.getData();
    }

    void deleteStation(Long id) {
        given().when().
            delete("/stations/" + id).
            then().
            log().all();
    }

    StandardResponse<LineResponse> createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", "bg-green-500");
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        return
            given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().as(new TypeRef<StandardResponse<LineResponse>>() {
            });
    }

    LineDetailResponse getLine(Long id) {
        StandardResponse<LineDetailResponse> response = given().
            when().
            get("/lines/" + id).
            then().
            log().all().
            extract().as(new TypeRef<StandardResponse<LineDetailResponse>>() {
        });
        return response.getData();
    }

    void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        Map<String, String> params = new HashMap<>();
        params.put("color", "bg-green-500");
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            put("/lines/" + id).
            then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    StandardResponse<List<LineResponse>> getLines() {
        return
            given().when().
                get("/lines").
                then().
                log().all().
                extract().
                as(new TypeRef<StandardResponse<List<LineResponse>>>() {
                });
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
            post("/lines/" + lineId + "/stations").
            then().
            log().all().
            statusCode(HttpStatus.CREATED.value());
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

