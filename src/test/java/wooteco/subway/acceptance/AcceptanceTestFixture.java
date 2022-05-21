package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.SectionRequest;

public class AcceptanceTestFixture {

    public static Long createLine(String name, String color, Long upStationId, Long downStationId,
        int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId,
            distance);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();

        String uri = response.header("Location");
        return Long.parseLong(uri.split("/")[2]);
    }

    public static void createSection(Long upStationId, Long downStationId, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(sectionRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + 1L + "/sections")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static Long createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();

        String uri = response.header("Location");
        return Long.parseLong(uri.split("/")[2]);
    }
}
