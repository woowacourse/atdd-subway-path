package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    private Long createdStationId1;
    private Long createdStationId2;

    @BeforeEach
    void init() {
        createdStationId1 = AcceptanceUtil.createStation("강남역");
        createdStationId2 = AcceptanceUtil.createStation("잠실역");
        AcceptanceUtil.createLine("2호선", "bg-red-600", createdStationId1, createdStationId2, 10);
    }

    @DisplayName("출발역부터 도착역까지의 최단 경로와 요금 및 거리를 조회한다.")
    @Test
    void get_path() {
        // given
        Long departureStationId = createdStationId1;
        Long arrivalStationId = createdStationId2;

        // when
        ExtractableResponse<Response> response = requestGetPath(departureStationId, arrivalStationId);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getInt("distance")).isEqualTo(10),
                () -> assertThat(response.body().jsonPath().getInt("fare")).isEqualTo(1250),
                () -> assertThat(
                        response.body().jsonPath().getList("stations", StationResponse.class).size()).isEqualTo(2)
        );
    }

    private ExtractableResponse<Response> requestGetPath(Long departureStationId, Long arrivalStationId) {
        return RestAssured.given().log().all()
                .when()
                .get("/paths?source=" + departureStationId + "&target=" + arrivalStationId)
                .then().log().all()
                .extract();
    }
}
