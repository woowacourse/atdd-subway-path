package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("역과 노선을 등록하고 최단 거리를 계산한다")
    @Test
    void findShortestPath() {
        Station 강남역 = createStation("강남역").as(Station.class);
        Station 홍대입구역 = createStation("홍대입구역").as(Station.class);
        Station 선릉역 = createStation("선릉역").as(Station.class);
        Station 잠실역 = createStation("잠실역").as(Station.class);

        LineResponse 일호선 = createLine("1호선", "red", 강남역.getId(), 선릉역.getId(), 100).as(LineResponse.class);
        LineResponse 이호선 = createLine("2호선", "green", 강남역.getId(), 홍대입구역.getId(), 200).as(LineResponse.class);
        LineResponse 삼호선 = createLine("3호선", "blue", 강남역.getId(), 잠실역.getId(), 300).as(LineResponse.class);

        addSection(일호선.getId(), 선릉역.getId(), 홍대입구역.getId(), 78);
        addSection(삼호선.getId(), 잠실역.getId(), 홍대입구역.getId(), 5);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParams(Map.of("source", 강남역.getId(), "target", 홍대입구역.getId(), "age", 10))
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public ExtractableResponse<Response> createStation(String name) {
        Map<String, Object> params = Map.of("name", name);
        return post("/stations", params);
    }

    private ExtractableResponse<Response> createLine(String name, String color, Long upStationsId, Long downStationId,
                                                     int distance) {
        Map<String, Object> params = Map.of(
                "name", name,
                "color", color,
                "upStationId", upStationsId,
                "downStationId", downStationId,
                "distance", distance);
        return post("/lines", params);
    }

    private ExtractableResponse<Response> addSection(Long lineId, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = Map.of(
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance);
        return post("/lines/" + lineId + "/sections", params);
    }
}
