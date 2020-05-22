package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        return post("/stations", params, StationResponse.class);
    }

    List<StationResponse> getStations() {
        return getList("/stations", StationResponse.class);
    }

    void deleteStation(Long id) {
        delete("/stations/" + id);
    }

    LineResponse createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        return post("/lines", params, LineResponse.class);
    }

    LineDetailResponse getLine(Long id) {
        return get("/lines/" + id, LineDetailResponse.class);
    }

    void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        put("/lines/" + id, params);
    }

    List<LineResponse> getLines() {
        return getList("/lines", LineResponse.class);
    }

    void deleteLine(Long id) {
        delete("/lines/" + id);
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
                post("/lines/" + lineId + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    void removeLineStation(Long lineId, Long stationId) {
        delete("/lines/" + lineId + "/stations/" + stationId);
    }

    void setSubwayInformation() {
        LineResponse lineResponse2 = createLine("2호선");
        LineResponse lineResponse7 = createLine("7호선");
        LineResponse lineResponseB = createLine("분당선");

        StationResponse 왕십리 = createStation("왕십리");
        StationResponse 한양대 = createStation("한양대");
        StationResponse 뚝섬 = createStation("뚝섬");
        StationResponse 성수 = createStation("성수");
        StationResponse 건대입구 = createStation("건대입구");
        StationResponse 뚝섬유원지 = createStation("뚝섬유원지");
        StationResponse 청담 = createStation("청담");
        StationResponse 강남구청 = createStation("강남구청");
        StationResponse 압구정로데오 = createStation("압구정로데오");
        StationResponse 서울숲 = createStation("서울숲");
        StationResponse 잠실 = createStation("잠실");

        addLineStation(lineResponse2.getId(), null, 왕십리.getId(), 0, 0);
        addLineStation(lineResponse2.getId(), 왕십리.getId(), 한양대.getId(), 5, 2);
        addLineStation(lineResponse2.getId(), 한양대.getId(), 뚝섬.getId(), 5, 2);
        addLineStation(lineResponse2.getId(), 뚝섬.getId(), 성수.getId(), 5, 2);
        addLineStation(lineResponse2.getId(), 성수.getId(), 건대입구.getId(), 5, 2);

        addLineStation(lineResponse7.getId(), null, 건대입구.getId(), 0, 0);
        addLineStation(lineResponse7.getId(), 건대입구.getId(), 뚝섬유원지.getId(), 7, 4);
        addLineStation(lineResponse7.getId(), 뚝섬유원지.getId(), 청담.getId(), 7, 4);
        addLineStation(lineResponse7.getId(), 청담.getId(), 강남구청.getId(), 7, 4);

        addLineStation(lineResponseB.getId(), null, 강남구청.getId(), 0, 0);
        addLineStation(lineResponseB.getId(), 강남구청.getId(), 압구정로데오.getId(), 3, 1);
        addLineStation(lineResponseB.getId(), 압구정로데오.getId(), 서울숲.getId(), 3, 1);
        addLineStation(lineResponseB.getId(), 서울숲.getId(), 왕십리.getId(), 3, 1);
    }

    <T> T get(String path, Class<T> responseType) {
        return
                given().
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        get(path).
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(responseType);
    }

    <T> List<T> getList(String path, Class<T> responseType) {
        return
                given().when().
                        get(path).
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", responseType);
    }

    <T> T post(String path, Map<String, String> params, Class<T> responseType) {
        return
                given().
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

    <T> void put(String path, Map<String, String> params) {
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

    <T> void delete(String path) {
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

