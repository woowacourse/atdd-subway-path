package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {
    static final String STATION_NAME_KANGNAM = "강남역";
    static final String STATION_NAME_YEOKSAM = "역삼역";
    static final String STATION_NAME_SEOLLEUNG = "선릉역";
    static final String STATION_NAME_SAMSUNG = "삼성역";
    static final String STATION_NAME_SPORTS_COMPLEX = "종합운동장역";
    static final String STATION_NAME_JAMSILSAENAE = "잠실새내역";
    static final String STATION_NAME_JAMSIL = "잠실역";
    static final String STATION_NAME_YANGJAE = "양재역";
    static final String STATION_NAME_MAEBONG = "매봉역";
    static final String STATION_NAME_DOGOK = "도곡역";
    static final String STATION_NAME_DAECHI = "대치역";
    static final String STATION_NAME_HANGNYEOUL = "학여울역";
    static final String STATION_NAME_DAECHUNG = "대청역";
    static final String STATION_NAME_SUSEO = "수서역";
    static final String STATION_NAME_GARAK_MARKET = "가락시장역";
    static final String STATION_NAME_SONGPA = "송파역";
    static final String STATION_NAME_SEOKCHON = "석촌역";
    static final String STATION_NAME_YANGJAE_CITIZEN_FOREST = "양재시민의숲역";
    static final String STATION_NAME_CHEONGGYESAN = "청계산입구역";
    static final String STATION_NAME_PANGYO = "판교역";
    static final String STATION_NAME_JUNGJA = "정자역";
    static final String STATION_NAME_HANTI = "한티역";
    static final String STATION_NAME_GURYONG = "구룡역";
    static final String STATION_NAME_GAEPODONG = "개포동역";
    static final String STATION_NAME_DAEMOSAN = "대모산입구역";

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

    LineResponse createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
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
                        extract().as(LineResponse.class);
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
                        post("/stations").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(StationResponse.class);
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

}
