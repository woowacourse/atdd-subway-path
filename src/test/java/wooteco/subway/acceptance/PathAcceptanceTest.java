package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.SectionRequest;

import static wooteco.subway.acceptance.AcceptanceTestFixture.*;


class PathAcceptanceTest extends AcceptanceTest {

    @Test
    void findShortestPath() {
        // given
        createStation("강남역");
        createStation("교대역");
        createStation("역삼역");
        createStation("선릉역");

        createLine(new LineRequest("3호선", "bg-orange-600", 1L, 2L, 3));

        createSection(1L, new SectionRequest(2L, 3L, 4));
        createSection(1L, new SectionRequest(3L, 4L, 5));

        // when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1)
                .queryParam("target", 4)
                .queryParam("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
