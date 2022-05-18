package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixtures.GANGNAM;
import static wooteco.subway.Fixtures.HYEHWA;
import static wooteco.subway.Fixtures.LINE_2;
import static wooteco.subway.Fixtures.RED;
import static wooteco.subway.Fixtures.SINSA;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("경로 관리 API")
public class PathAcceptanceTest extends AcceptanceTest {

    /*
      Scenario: 경로 조회
          when: 경로 조회를 요청한다.
          then: 최단 경로와 요금을 계산한다.
          And: 경로에 포함된 지하철 역들과 최단 거리, 요금을 응답한다.
     */

    @Test
    @DisplayName("출발역과 도착역 사이의 경로를 조회한다.")
    void find() {
        // given
        final Long stationId1 = createStation(HYEHWA);
        final Long stationId2 = createStation(SINSA);
        final Long stationId3 = createStation(GANGNAM);
        final Long lineId = createLine(LINE_2, RED, stationId1, stationId2, 10);
        createSection(lineId, stationId2, stationId3, 10);

        // when
        final ExtractableResponse<Response> response = getPath(stationId1, stationId3, 29);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /*
      Scenario: 경로 조회 실패
           when: 경로 조회를 요청한다.
           if: 출발역과 도착역이 같다.
           then: 에러 메시지를 응답한다.
     */
    @Test
    @DisplayName("출발역과 도착역이 같을 때 응답을 실패한다.")
    void find_sameStation() {
        // given
        final Long stationId1 = createStation(HYEHWA);
        final Long stationId2 = createStation(SINSA);
        final Long stationId3 = createStation(GANGNAM);
        final Long lineId = createLine(LINE_2, RED, stationId1, stationId2, 10);
        createSection(lineId, stationId2, stationId3, 10);

        // when
        final ExtractableResponse<Response> response = getPath(stationId1, stationId1, 29);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
      Scenario: 경로 조회 실패
           when: 경로 조회를 요청한다.
           if: 출발역과 도착역 사이의 경로가 없다.
           then: 에러 메시지를 응답한다.
     */
    @Test
    @DisplayName("출발역과 도착역 사이의 경로가 없을 때 응답을 실패한다.")
    void find_noPath() {
        // given
        final Long stationId1 = createStation(HYEHWA);
        final Long stationId2 = createStation(SINSA);
        final Long stationId3 = createStation(GANGNAM);
        createLine(LINE_2, RED, stationId1, stationId2, 10);

        // when
        final ExtractableResponse<Response> response = getPath(stationId1, stationId3, 29);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> getPath(Long source, Long target, int age) {
        final Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        params.put("age", age);

        return RestAssured.given().log().all()
                .params(params)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    private Long createStation(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract()
                .body().jsonPath().getLong("id");
    }

    private Long createLine(final String name, final String color, final Long upStationId, final Long downStationId,
                            final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return Long.parseLong(RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract()
                .header("Location").split("/")[2]);
    }

    private void createSection(final Long lineId, final Long upStationId, final Long downStationId,
                               final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
