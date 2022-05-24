package wooteco.subway.acceptance.fixture;

import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class SimpleRestAssured {

    public static SimpleResponse get(String path) {
        return new SimpleResponse(given()
                .when().get(path));
    }

    public static SimpleResponse post(String path, Object object) {
        return new SimpleResponse(given()
                .body(object)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path));
    }

    public static SimpleResponse put(String path, Map<String, String> params) {
        return new SimpleResponse(given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path));
    }

    public static SimpleResponse delete(String path) {
        return new SimpleResponse(given()
                .when().delete(path));
    }

    private static RequestSpecification given() {
        return RestAssured.given().log().all();
    }
}
