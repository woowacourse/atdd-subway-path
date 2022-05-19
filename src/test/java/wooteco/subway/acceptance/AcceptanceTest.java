package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AcceptanceTest {

    protected static final String STATION_URL_PREFIX = "/stations";
    protected static final String LINE_URL_PREFIX = "/lines";
    protected static final String SECTION_URL_PREFIX = "/sections";
    protected static final String PATH_URL_PREFIX = "/paths";
    protected static final String LOCATION = "Location";

    protected static final String GANGNAM = "강남역";
    protected static final String YEOKSAM = "역삼역";
    protected static final String SEOLLEUNG = "선릉역";
    protected static final String SAMSUNG = "삼성역";
    protected static final String SEOUL_FOREST = "서울숲역";
    protected static final String WANGSIMNI = "왕십리역";
    protected static final String HEANGDANG = "행당역";
    protected static final String MAJANG = "마장역";
    protected static final String DAPSIMNI = "답십리역";
    protected static final String YACKSU = "약수역";
    protected static final String GEUMHO = "금호역";
    protected static final String OKSU = "옥수역";

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM section");
        jdbcTemplate.execute("DELETE FROM station");
        jdbcTemplate.execute("DELETE FROM line");
    }

    protected ValidatableResponse requestGet(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all();
    }

    protected ValidatableResponse requestPost(final Object request, final String url) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(url)
                .then().log().all();
    }

    protected ValidatableResponse requestPostSection(final Object request, final Long lineId) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(LINE_URL_PREFIX + "/" + lineId + SECTION_URL_PREFIX)
                .then().log().all();
    }

    protected ValidatableResponse requestPut(final Object request, final String url) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(url)
                .then().log().all();
    }

    protected ValidatableResponse requestDelete(final String url) {
        return RestAssured.given().log().all()
                .when()
                .delete(url)
                .then().log().all();
    }

    protected ValidatableResponse requestDeleteSection(final Long lineId, final Long stationId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", stationId)
                .when()
                .delete(LINE_URL_PREFIX + "/" + lineId + SECTION_URL_PREFIX)
                .then().log().all();
    }

    protected long findId(final ValidatableResponse response) {
        return Long.parseLong(response.extract()
                .header(LOCATION)
                .split("/")[2]);
    }

    protected long createAndGetId(final Object request, final String url) {
        return Long.parseLong(requestPost(request, url)
                .extract()
                .header(LOCATION)
                .split("/")[2]
        );
    }
}
