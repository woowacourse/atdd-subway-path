package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class AcceptanceFixture {

    public static Long createStation(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract()
                .body().jsonPath().getLong("id");
    }

    public static ExtractableResponse<Response> createLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static Long createLine(final String name, final String color, final Long upStationId,
                                  final Long downStationId, final int distance, final int extraFare) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("extraFare", extraFare);

        return Long.parseLong(RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract()
                .header("Location").split("/")[2]);
    }

    public static ExtractableResponse<Response> updateLine(long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(long id) {
        return RestAssured.given().log().all()
                .when()
                .delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static void createSection(final Long lineId, final Long upStationId, final Long downStationId,
                                     final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
