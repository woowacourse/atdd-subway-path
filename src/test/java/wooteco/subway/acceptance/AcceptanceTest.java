package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    private <T> RequestSpecification createBody(T request) {
        return RestAssured.given()
            .log()
            .all()
            .body(request);
    }

    ExtractableResponse<Response> requestHttpGet(String uri) {
        return RestAssured.given().log().all()
            .when()
            .get(uri)
            .then().log().all()
            .extract();
    }

    <T> ExtractableResponse<Response> requestHttpPost(T request, String uri) {
        return createBody(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(uri)
            .then().log().all()
            .extract();
    }

    <T> ExtractableResponse<Response> requestHttpPut(T request, String uri) {
        return createBody(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put(uri)
            .then().log().all()
            .extract();
    }

    ExtractableResponse<Response> requestHttpDelete(String uri) {
        return RestAssured.given().log().all()
            .when()
            .delete(uri)
            .then().log().all()
            .extract();
    }

    <T> ExtractableResponse<Response> requestHttpDelete(T request, String uri) {
        return createBody(request)
            .when()
            .delete(uri)
            .then().log().all()
            .extract();
    }
}
