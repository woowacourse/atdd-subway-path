package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathAcceptanceTest extends AcceptanceTest {

    private Long 강남역_ID;
    private Long 역삼역_ID;
    private Long 선릉역_ID;
    private Long 잠실역_ID;
    private Long 마라도_ID;
    private Long 호선2_ID;
    private Long 호선3_ID;
    private int 나이 = 10;

    @BeforeEach
    void setUpData() {
        강남역_ID = createStation("강남역");
        역삼역_ID = createStation("역삼역");
        선릉역_ID = createStation("선릉역");
        잠실역_ID = createStation("잠실역");
        마라도_ID = createStation("마라도역");
        호선2_ID = createLine("2호선", "bg-red-600", 강남역_ID, 역삼역_ID, 10);
        호선3_ID = createLine("3호선", "bg-green-600", 강남역_ID, 잠실역_ID, 9);
        createSection(호선2_ID, 역삼역_ID, 선릉역_ID, 5);
        createSection(호선3_ID, 잠실역_ID, 선릉역_ID, 8);
    }

    @Test
    @DisplayName("강남역에서 선릉역까지 가는 경로를 테스트한다.")
    void pathTest() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source=" + 강남역_ID + "&target=" + 선릉역_ID + "&age=" + 나이)
                .then().log().all()
                .extract();

        List<StationResponse> stations = response.body().jsonPath().getList("stations", StationResponse.class);
        List<String> stationNames = stations.stream()
                .map(stationResponse -> stationResponse.getName())
                .collect(Collectors.toList());
        int distance = response.body().jsonPath().getInt("distance");
        int fare = response.body().jsonPath().getInt("fare");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).containsExactly("강남역", "역삼역", "선릉역"),
                () -> assertThat(distance).isEqualTo(15),
                () -> assertThat(fare).isEqualTo(1350)
        );
    }

    @Test
    @DisplayName("존재하지 않는 역을 조회하는 경우 상태코드 400을 반환한다.")
    void notExistStation() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source=" + 9999 + "&target=" + 선릉역_ID + "&age=" + 나이)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("출발지에서 도착지로 갈 수 없는 경우 상태코드 400을 반환한다.")
    void noReachableStation() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source=" + 마라도_ID + "&target=" + 선릉역_ID + "&age=" + 나이)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("출발지에서 도착지로 갈 수 있느 경로가 여러개인 경우 최단 길이의 경로를 반환한다.")
    void calculateShortestPathWithManyCase() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?source=" + 잠실역_ID + "&target=" + 역삼역_ID + "&age=" + 나이)
                .then().log().all()
                .extract();

        List<StationResponse> stations = response.body().jsonPath().getList("stations", StationResponse.class);
        List<String> stationNames = stations.stream()
                .map(stationResponse -> stationResponse.getName())
                .collect(Collectors.toList());
        int distance = response.body().jsonPath().getInt("distance");
        int fare = response.body().jsonPath().getInt("fare");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).containsExactly("잠실역", "선릉역", "역삼역"),
                () -> assertThat(distance).isEqualTo(13),
                () -> assertThat(fare).isEqualTo(1350)
        );

    }

    private ExtractableResponse<Response> createSection(final Long lineId, final Long upStationId,
                                                        final Long downStationId, final Integer distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private Long createLine(final String name, final String color, final Long upStationId,
                            final Long downStationId, final Integer distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        return Long.parseLong(response.header("location").split("/")[2]);
    }

    private Long createStation(final String name) {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        return Long.parseLong(response.header("location").split("/")[2]);
    }
}
