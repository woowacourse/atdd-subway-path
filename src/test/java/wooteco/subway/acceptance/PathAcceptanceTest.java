package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.dto.response.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("최단 거리와 최단 경로를 응답한다.")
    @Test
    void getShortestPath() {
        createStationForTest("신림역");
        createStationForTest("강남역");
        createStationForTest("역삼역");
        createStationForTest("선릉역");
        createStationForTest("잠실역");

        ExtractableResponse<Response> response1 = RequestFrame.post(
            BodyCreator.makeLineBodyForPost("1호선", "blue", "1", "2", "3", "900"),
            "/lines"
        );
        ExtractableResponse<Response> response2 = RequestFrame.post(
            BodyCreator.makeLineBodyForPost("2호선", "green", "2", "3", "4", "900"),
            "/lines"
        );
        ExtractableResponse<Response> response3 = RequestFrame.post(
            BodyCreator.makeLineBodyForPost("3호선", "orange", "1", "3", "10", "900"),
            "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.get("/paths?source=1&target=3&age=15");

        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(pathResponse.getStations()).hasSize(3),
            () -> assertThat(pathResponse.getDistance()).isEqualTo(7),
            () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    private void createStationForTest(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RequestFrame.post(params, "/stations");
    }
}
