package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.MediaType;

public class RestAssuredConvenienceMethod {

    public static ValidatableResponse postRequest(Object body, String path) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all();
    }

    public static Long postRequestAndGetId(Object body, String path) {
        Object id = RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract()
                .path("id");
        return Long.parseLong(String.valueOf(id));
    }

    public static ValidatableResponse getRequest(String path) {
        return RestAssured.given().log().all()
                .when()
                .get(path)
                .then().log().all();
    }

    public static ValidatableResponse putRequest(Object body, String path) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all();
    }

    public static ValidatableResponse deleteRequest(String path) {
        return RestAssured.given().log().all()
                .when()
                .delete(path)
                .then().log().all();
    }
}
