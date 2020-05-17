package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PageAcceptanceTest extends AcceptanceTest{
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @Test
    void linePage() {
        createLine("신분당선");

        given().
                accept(MediaType.TEXT_HTML_VALUE).
        when().
                get("/admin-line").
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }
}
