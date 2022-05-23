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

        createLineForTest("1호선", "blue", "1", "2", "3", "900");
        createLineForTest("2호선", "green", "2", "3", "4", "900");
        createLineForTest("3호선", "orange", "1", "3", "10", "900");

        ExtractableResponse<Response> response = RequestFrame.get("/paths?source=1&target=3&age=15");

        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(pathResponse.getStations()).hasSize(3),
            () -> assertThat(pathResponse.getDistance()).isEqualTo(7),
            () -> assertThat(pathResponse.getFare()).isEqualTo(1440)
        );
    }

    @DisplayName("환승을 포하함 요금을 계산한다.")
    @Test
    void getShortestPathWithChangeLine() {
        createStationForTest("교대역");    // id: 1
        createStationForTest("강남역");    // id: 2
        createStationForTest("역삼역");    // id: 3
        createStationForTest("선릉역");    // id: 4
        createStationForTest("양재역");    // id: 5
        createStationForTest("매봉역");    // id: 6
        createStationForTest("도곡역");    // id: 7

        createLineForTest("2호선", "green", "1", "2", "3", "900");
        createLineForTest("3호선", "orange", "1", "5", "2", "900");
        createLineForTest("수인분당선", "yellow", "7", "4", "1", "900");
        createLineForTest("신분당선", "red", "2", "6", "1", "900");

        createSectionForTest(1, "2", "3", "2");
        createSectionForTest(1, "3", "4", "2");
        createSectionForTest(2, "5", "6", "1");
        createSectionForTest(2, "6", "7", "1");

        ExtractableResponse<Response> response = RequestFrame.get("/paths?source=2&target=4&age=15");

        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(pathResponse.getStations()).hasSize(4),
            () -> assertThat(pathResponse.getDistance()).isEqualTo(3),
            () -> assertThat(pathResponse.getFare()).isEqualTo(1440)
        );
    }

    private void createStationForTest(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response = RequestFrame.post(params, "/stations");
    }

    private void createLineForTest(String name, String color, String upStationId, String downStationId, String distance,
        String extraFare) {
        Map<String, String> body = BodyCreator.makeLineBodyForPost(name, color, upStationId, downStationId,
            distance, extraFare);
        ExtractableResponse<Response> response = RequestFrame.post(body, "/lines");
    }

    private void createSectionForTest(int id, String upStationId, String downStationId, String distance) {
        Map<String, String> body = new HashMap<>();
        body.put("upStationId", upStationId);
        body.put("downStationId", downStationId);
        body.put("distance", distance);

        ExtractableResponse<Response> response = RequestFrame.post(body, "/lines/" + id + "/sections");
    }
}
