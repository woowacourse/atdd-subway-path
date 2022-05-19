package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void createStations(){
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        createStationResponseOf(params1);

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        createStationResponseOf(params2);

        Map<String, String> params3 = new HashMap<>();
        params3.put("name", "선릉역");
        createStationResponseOf(params3);

        Map<String, Object> params4 = new HashMap<>();
        params4.put("name", "1호선");
        params4.put("color", "red");
        params4.put("upStationId", 1L);
        params4.put("downStationId", 3L);
        params4.put("distance", 10);

        createLineResponseOf(params4);

        Map<String, Object> params5 = new HashMap<>();
        params5.put("upStationId", 3L);
        params5.put("downStationId", 2L);
        params5.put("distance", 10);

        createSectionResponseOf(params5);
    }

    @DisplayName("최단경로를 조회한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/paths?source=1&target=2&age=25")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("20");
        assertThat(response.body().asString()).contains("1450");
    }

    private void createStationResponseOf(Map<String, String> params) {
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private void createLineResponseOf(Map<String, Object> params) {
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private void createSectionResponseOf(Map<String, Object> params) {
        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/1/sections")
                .then().log().all()
                .extract();
    }
}
