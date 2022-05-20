package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("delete from section");
        jdbcTemplate.update("delete from line");
        jdbcTemplate.update("delete from station");
    }

    protected long 역_저장(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> savedResponse = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
        return savedResponse.body().jsonPath().getLong("id");
    }

    protected long 노선_저장(Map<Object, Object> 노선_저장_파라미터) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(노선_저장_파라미터)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
        return response.body().jsonPath().getLong("id");
    }

    protected ExtractableResponse<Response> 노선_저장_응답(Map<Object, Object> 노선_요청_파라미터) {
        return RestAssured.given().log().all()
                .body(노선_요청_파라미터)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> 노선_조회(long id) {
        return RestAssured.given().log().all()
                .when()
                .get("/lines/{id}", id)
                .then()
                .log().all().extract();
    }

    protected ExtractableResponse<Response> 노선_목록_조회() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse<Response> 노선_수정(Long lineId, Map<Object, Object> 노선_수정_파라미터) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(노선_수정_파라미터)
                .when()
                .put("/lines/{id}", lineId)
                .then()
                .log().all().extract();
    }

    protected ExtractableResponse<Response> 노선_삭제(long id) {
        return RestAssured.given().log().all()
                .when()
                .delete("/lines/{id}", id)
                .then()
                .log().all().extract();
    }

    protected ExtractableResponse<Response> 구간_등록(long lineId, Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{id}/sections", lineId)
                .then().log().all()
                .extract();
        return response;
    }

    protected ExtractableResponse<Response> 구간_삭제(long lineId, long deleteStationId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("stationId", deleteStationId)
                .delete("/lines/{id}/sections", lineId)
                .then().log().all()
                .extract();
    }

    protected Map<Object, Object> 노선_저장_파라미터(String name, String color, Long upStationId,
            Long downStationId, int distance) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    protected Map<Object, Object> 노선_수정_파라미터(String name, String color) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    protected Map<String, Object> 구간_등록_파라미터(long upStationId, long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }
}
