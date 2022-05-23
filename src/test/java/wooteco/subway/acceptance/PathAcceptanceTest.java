package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    private Long createdStationId1;
    private Long createdStationId2;

    @BeforeEach
    void init() {
        createdStationId1 = AcceptanceUtil.createStation("강남역");
        createdStationId2 = AcceptanceUtil.createStation("잠실역");
        AcceptanceUtil.createLine("2호선", "bg-red-600", 600, createdStationId1, createdStationId2, 10);
    }

    @DisplayName("출발역부터 도착역까지의 최단 경로를 조회한다.")
    @Test
    void getShortedPath() {
        // given
        Long departureStationId = createdStationId1;
        Long arrivalStationId = createdStationId2;

        // when
        ExtractableResponse<Response> response = requestGetPath(departureStationId, arrivalStationId, 20);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(
                        response.body().jsonPath().getList("stations", StationResponse.class).size()).isEqualTo(2)
        );
    }

    @DisplayName("최단거리에 따른 요금을 연령, 추가운임, 거리를 통해 계산한다.")
    @CsvSource(value = {"20,0,10,1250", "8,0,10,450", "16,0,10,720", "20,600,10,1850", "8,600,10,750",
            "16,600,10,1200", "20,500,20,1950", "8,500,20,800", "16,500,20,1280", "1,1000,100,0"})
    @ParameterizedTest(name = "[{index}] 만 {0}세, 추가운임 {1}원, {2}km => {3}원")
    void getFareOfShortestPath(int age, int extraFare, int distance, int expectedFare) {
        // given
        Long departureStationId = AcceptanceUtil.createStation("출발역");
        Long arrivalStationId = AcceptanceUtil.createStation("도착역");
        AcceptanceUtil.createLine("추가 운임 없는 호선", "bg-red-600", extraFare, departureStationId, arrivalStationId,
                distance);

        // when
        ExtractableResponse<Response> response = requestGetPath(departureStationId, arrivalStationId, age);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getInt("fare")).isEqualTo(expectedFare)
        );
    }

    private ExtractableResponse<Response> requestGetPath(Long departureStationId, Long arrivalStationId, Integer age) {
        return RestAssured.given().log().all()
                .when()
                .get("/paths?source=" + departureStationId + "&target=" + arrivalStationId + "&age=" + age)
                .then().log().all()
                .extract();
    }
}
