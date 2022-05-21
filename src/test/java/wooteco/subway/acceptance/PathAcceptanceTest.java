package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.controller.response.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("최단 거리와 최단 경로를 응답한다.")
    @Test
    void getShortestPath() {
        createStation("신림역");
        createStation("강남역");
        createStation("역삼역");
        createStation("선릉역");
        createStation("잠실역");

        createLine("1호선", "blue", "1", "2", "3", "900");
        createLine("2호선", "green", "2", "3", "4", "900");
        createLine("3호선", "orange", "1", "3", "10", "900");

        ExtractableResponse<Response> response = RequestFrame.get("/paths?source=1&target=3&age=15");

        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(7),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("거리가 같은 경우 더 적은 역을 거치도록 응답한다.")
    @Test
    void getShortestPathSameDistance() {
        createStation("교대역");    // id: 1
        createStation("강남역");    // id: 2
        createStation("역삼역");    // id: 3
        createStation("선릉역");    // id: 4
        createStation("양재역");    // id: 5
        createStation("매봉역");    // id: 6
        createStation("도곡역");    // id: 7

        createLine("2호선", "green", "1", "2", "3", "900");
        createLine("3호선", "orange", "1", "5", "2", "900");
        createLine("수인분당선", "yellow", "7", "4", "1", "900");
        createLine("신분당선", "red", "2", "6", "1", "900");

        createSection(1, "2", "3", "2");
        createSection(1, "3", "4", "2");
        createSection(2, "5", "6", "1");
        createSection(2, "6", "7", "1");

        ExtractableResponse<Response> response = RequestFrame.get("/paths?source=2&target=4&age=15");

        PathResponse pathResponse = response.body().jsonPath().getObject(".", PathResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(pathResponse.getStations()).hasSize(4),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(3),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("두 역이 연결되어있지 않으면 상태코드 404를 응답한다.")
    @Test
    void notFoundPath() {
        createStation("신림역");
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");

        createLine("1호선", "blue", "1", "2", "3", "900");
        createLine("2호선", "green", "3", "4", "4", "900");

        ExtractableResponse<Response> response = RequestFrame.get("/paths?source=1&target=3&age=30");

        String message = response.body().asString();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(message).contains("이동할 수 있는 경로가 없습니다.")
        );
    }

    private void createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        RequestFrame.post(params, "/stations");
    }

    private void createLine(String name, String color, String upStationId, String downStationId, String distance,
                            String extraFare) {
        Map<String, String> body = BodyCreator.makeLineBodyForPost(name, color, upStationId, downStationId,
                distance, extraFare);
        RequestFrame.post(body, "/lines");
    }

    private void createSection(int id, String upStationId, String downStationId, String distance) {
        Map<String, String> body = new HashMap<>();
        body.put("upStationId", upStationId);
        body.put("downStationId", downStationId);
        body.put("distance", distance);

        RequestFrame.post(body, "/lines/" + id + "/sections");
    }
}
