package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.response.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("출발역과 도착역으로 최단 경로를 조회한다.")
    void getPath() {
        // given
        Map<String, Object> stationParams1 = Map.of("name", "강남역");
        Map<String, Object> stationParams2 = Map.of("name", "역삼역");
        Map<String, Object> stationParams3 = Map.of("name", "선릉역");
        SimpleRestAssured.post("/stations", stationParams1);
        SimpleRestAssured.post("/stations", stationParams2);
        SimpleRestAssured.post("/stations", stationParams3);

        // 노선 등록
        Map<String, Object> lineParams = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", "1",
                "downStationId", "2",
                "distance", "10",
                "extraFare", 900
        );
        SimpleRestAssured.post("/lines", lineParams);

        // 구간 등록
        Map<String, Object> sectionParams =
                Map.of("upStationId", "2",
                        "downStationId", "3",
                        "distance", "7");
        SimpleRestAssured.post("/lines/1/sections", sectionParams);

        // 경로 조회
        Map<String, Integer> params = Map.of("source", 1, "target", 3, "age", 15);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .params(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths")
                .then().log().all().extract();

        JsonPath jsonPath = response.body().jsonPath();
        PathResponse pathResponse = jsonPath.getObject(".", PathResponse.class);

        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(17),
                () -> assertThat(pathResponse.getStationResponses()).hasSize(3),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1600)
        );
    }
}
