package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    private <T> RequestSpecification createBody(final T request) {
        return RestAssured.given()
                .log()
                .all()
                .body(request);
    }

    ExtractableResponse<Response> requestHttpGet(final String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    <T> ExtractableResponse<Response> requestHttpPost(final T request, final String uri) {
        return createBody(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    <T> ExtractableResponse<Response> requestHttpPut(final T request, final String uri) {
        return createBody(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> requestHttpDelete(final String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    <T> ExtractableResponse<Response> requestHttpDelete(final T request, final String uri) {
        return createBody(request)
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
