package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("최단 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("없는 역에 대한 최단 경로를 조회한다.")
    @Test
    void createShortestPathNotFound() {
        // given
        Long 사우역 = saveStation("사우역");
        Long 풍무역 = saveStation("풍무역");
        Long 없는역 = 111L;
        // when
        Map<String, Object> param = lineParam("육호선", "white", 풍무역, 사우역);
        Long 노선 = createLine(param);

        // then

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 풍무역)
                .queryParam("target", 없는역)
                .queryParam("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    @DisplayName("환승을 고려한 최단 경로를 조회한다.")
    @Test
    void createShortestPathWithTransfer() {
        // given
        Long 두번째역 = saveStation("두번째역");
        Long 첫번째역 = saveStation("첫번째역");
        Long 세번째역 = saveStation("세번째역");
        // when
        Map<String, Object> param1 = lineParam("1호선", "blue", 첫번째역, 세번째역);
        Long 일호선 = createLine(param1);
        Map<String, Object> param2 = lineParam("2호선", "pink", 세번째역, 두번째역);
        Long 이호선 = createLine(param2);
        // then

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 첫번째역)
                .queryParam("target", 두번째역)
                .queryParam("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private Map<String, Object> sectionParam(Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private void createSection(long id, Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .post("/lines/{id}/sections", id)
                .then()
                .log().all().extract();
    }

    private Long saveStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        return response.body().jsonPath().getLong("id");
    }

    private Map<String, Object> lineParam(String name, String color, Long upStationId,
                                          Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 16);
        return params;
    }

    private Long createLine(Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
        return response.body().jsonPath().getLong("id");
    }
}
