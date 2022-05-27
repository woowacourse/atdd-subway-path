package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("출발역과 도착역으로 최단 경로를 조회한다.")
    public void getPath() {
        // given
        StationRequest 강남역 = new StationRequest("강남역");
        StationRequest 역삼역 = new StationRequest("역삼역");
        StationRequest 선릉역 = new StationRequest("선릉역");
        SimpleRestAssured.post("/stations", 강남역);
        SimpleRestAssured.post("/stations", 역삼역);
        SimpleRestAssured.post("/stations", 선릉역);

        LineCreateRequest lineCreateRequest =
                new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        1L,
                        2L,
                        10,
                        900);
        SimpleRestAssured.post("/lines", lineCreateRequest);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 7);
        SimpleRestAssured.post("/lines/1/sections", sectionRequest);

        Map<String, Integer> params = Map.of("source", 1, "target", 3, "age", 15);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParams(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then().log().all().extract();

        JsonPath jsonPath = response.body().jsonPath();
        PathResponse pathResponse = jsonPath.getObject(".", PathResponse.class);

        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(17),
                () -> assertThat(pathResponse.getStationResponses()).hasSize(3),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1950)
        );
    }

}
