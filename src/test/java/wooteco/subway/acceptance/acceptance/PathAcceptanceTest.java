package wooteco.subway.acceptance.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.acceptance.AcceptanceFixture.LINE_URL;
import static wooteco.subway.acceptance.acceptance.AcceptanceFixture.STATION_URL;
import static wooteco.subway.acceptance.acceptance.AcceptanceFixture.getMethodRequest;
import static wooteco.subway.acceptance.acceptance.AcceptanceFixture.postMethodRequest;
import static wooteco.subway.acceptance.acceptance.AcceptanceFixture.강남역;
import static wooteco.subway.acceptance.acceptance.AcceptanceFixture.신분당선;
import static wooteco.subway.acceptance.acceptance.AcceptanceFixture.청계산입구역;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> createdResponse1;
    ExtractableResponse<Response> createdResponse2;

    @BeforeEach
    void init() {
        createdResponse1 = postMethodRequest(강남역, STATION_URL);
        createdResponse2 = postMethodRequest(청계산입구역, STATION_URL);

        postMethodRequest(신분당선, LINE_URL);
    }

    @DisplayName("출발역부터 도착역까지의 최단 경로와 요금 및 거리를 조회한다.")
    @Test
    void get_path() {
        // given
        String departureStationId = createdResponse1.header("Location").split("/")[2];
        String arrivalStationId = createdResponse2.header("Location").split("/")[2];

        // when
        ExtractableResponse<Response> response = getMethodRequest(
                "/paths?source=" + departureStationId + "&target=" + arrivalStationId);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getInt("distance")).isEqualTo(10),
                () -> assertThat(response.body().jsonPath().getInt("fare")).isEqualTo(1250),
                () -> assertThat(
                        response.body().jsonPath().getList("stations", StationResponse.class).size()).isEqualTo(2)
        );
    }
}
