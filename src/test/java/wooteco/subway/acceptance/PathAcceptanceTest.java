package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.response.StationResponse;

@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로를 조회한다.")
    @Test
    void findShortestPath() {
        final ExtractableResponse<Response> stationResponse1 = createStation("a");
        final ExtractableResponse<Response> stationResponse2 = createStation("b");
        final Long upStationId = stationResponse1.jsonPath().getObject(".", StationResponse.class).getId();
        final Long downStationId = stationResponse2.jsonPath().getObject(".", StationResponse.class).getId();
        createLineAfterCreatingStation("신분당선", "bg-red-600",
                upStationId, downStationId, 5);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .get("/paths?source=" + upStationId + "&target=" + downStationId + "&age=1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> createLineAfterCreatingStation(final String name, final String color,
                                                                         final Long upStationId,
                                                                         final Long downStationId,
                                                                         final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createStation(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }
}
