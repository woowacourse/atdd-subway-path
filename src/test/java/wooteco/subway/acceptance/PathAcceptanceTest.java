package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.equalTo;

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
        /*
        given
        강남역, 홍대입구역, 선릉역, 잠실역을 등록한다
        노선과 구간을 차례로 등록해 아래와 같은 세 노선이 된다
        1호선 : 강남-선릉-홍대입구
        2호선 : 강남-홍대입구
        3호선 : 강남-잠실-홍대입구

        when
        출발역, 도착역 : 강남역, 홍대입구역
        나이 : 10살

        then
        최적 경로 : 강남-선릉-홍대입구
        거리 : 178
        요금 : 2000원
        */
        Station 강남역 = createStation("강남역").as(Station.class);
        Station 홍대입구역 = createStation("홍대입구역").as(Station.class);
        Station 선릉역 = createStation("선릉역").as(Station.class);
        Station 잠실역 = createStation("잠실역").as(Station.class);

        LineResponse 일호선 = createLine("1호선", "red", 강남역.getId(), 선릉역.getId(), 100).as(LineResponse.class);
        LineResponse 이호선 = createLine("2호선", "green", 강남역.getId(), 홍대입구역.getId(), 200).as(LineResponse.class);
        LineResponse 삼호선 = createLine("3호선", "blue", 강남역.getId(), 잠실역.getId(), 300).as(LineResponse.class);

        addSection(일호선.getId(), 선릉역.getId(), 홍대입구역.getId(), 78);
        addSection(삼호선.getId(), 잠실역.getId(), 홍대입구역.getId(), 5);

        RestAssured.given().log().all()
                .queryParams(Map.of("source", 강남역.getId(), "target", 홍대입구역.getId(), "age", 10))
                .when()
                .get("/paths")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("stations[0].name", equalTo("강남역"))
                .body("stations[1].name", equalTo("선릉역"))
                .body("stations[2].name", equalTo("홍대입구역"))
                .body("distance", equalTo(178))
                .body("fare", equalTo(2000));
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
