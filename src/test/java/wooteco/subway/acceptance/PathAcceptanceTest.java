package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.dto.PathServiceResponse;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.SectionRequest;

class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("upStationId와 downStationId를 입력 받아, 두 역 간에 최단 경로, 거리, 요금을 조회한다.")
    void findShortestPath() {
        // given
        createStation("강남역");
        createStation("교대역");
        createStation("역삼역");
        createStation("선릉역");

        createLine("3호선", "bg-orange-600", 1L, 2L, 3);

        createSection(2L, 3L, 4);
        createSection(3L, 4L, 5);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1)
            .queryParam("target", 3)
            .queryParam("age", 20)
            .when()
            .get("/paths")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        PathServiceResponse result = jsonPath.getObject("", PathServiceResponse.class);
        assertAll(
            () -> assertThat(result.getStations().size()).isEqualTo(3),
            () -> assertThat(result.getFare()).isEqualTo(1250),
            () -> assertThat(result.getDistance()).isEqualTo(7)
        );
    }

    private void createLine(String name, String color, Long upStationId, Long downStationId,
        int distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId,
            distance);
        RestAssured.given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    private void createSection(Long upStationId, Long downStationId, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        RestAssured.given().log().all()
            .body(sectionRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + 1L + "/sections")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    void createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
    }
}
