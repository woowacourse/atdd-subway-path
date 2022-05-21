package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createLine;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createSection;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createStation;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.dto.PathResponse;

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
            .queryParam("source", 4)
            .queryParam("target", 1)
            .queryParam("age", 20)
            .when()
            .get("/paths")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        PathResponse result = jsonPath.getObject("", PathResponse.class);
        assertAll(
            () -> assertThat(result.getStations().size()).isEqualTo(4),
            () -> assertThat(result.getFare()).isEqualTo(1350),
            () -> assertThat(result.getDistance()).isEqualTo(12)
        );
    }
}
