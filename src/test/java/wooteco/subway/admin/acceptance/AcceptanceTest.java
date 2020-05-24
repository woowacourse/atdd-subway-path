package wooteco.subway.admin.acceptance;

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

    void post(Map<String, String> params, String path) {
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

    <T> List<T> getList(String path, Class<T> responseType) {
        return
            given().when().
                get(path).
                then().
                log().all()
                .extract()
                .jsonPath().getList(".", responseType);
    }

    <T> T get(String path, Class<T> responseType) {
        return
            given().when().
                get(path).
                then().
                log().all().
                extract().as(responseType);
    }

    void delete(String path) {
        given().when().
            delete(path).
            then().
            log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    void put(String path, Map<String, String> params) {
        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put(path)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}