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
import wooteco.subway.acceptance.fixture.SimpleResponse;
import wooteco.subway.acceptance.fixture.SubwayFixture;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.LineCreateResponse;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("출발역과 도착역으로 최단 경로를 조회한다.")
    public void getPath() {
        // given
        StationResponse 강남역 = SubwayFixture.createStation(new StationRequest("강남역")).toObject(StationResponse.class);
        StationResponse 역삼역 = SubwayFixture.createStation(new StationRequest("역삼역")).toObject(StationResponse.class);
        StationResponse 선릉역 = SubwayFixture.createStation(new StationRequest("선릉역")).toObject(StationResponse.class);

        SimpleResponse line = SubwayFixture.createLine(강남역, 역삼역);

        SubwayFixture.createSection(역삼역, 선릉역, line.toObject(LineCreateResponse.class).getId());

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
