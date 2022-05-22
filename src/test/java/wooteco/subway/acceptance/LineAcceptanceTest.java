package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.GREEN;
import static wooteco.subway.Fixtures.GANGNAM;
import static wooteco.subway.Fixtures.HYEHWA;
import static wooteco.subway.Fixtures.JAMSIL;
import static wooteco.subway.Fixtures.LINE_2;
import static wooteco.subway.Fixtures.LINE_4;
import static wooteco.subway.Fixtures.SKY_BLUE;
import static wooteco.subway.Fixtures.SUNGSHIN;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;

@Nested
@DisplayName("지하철 노선 관리 API")
public class LineAcceptanceTest extends AcceptanceTest {

    /*
        Scenario: 지하철 노선 등록
            When: 지하철 노선 등록을 요청한다.
            Then: 지하철 노선이 생성된다.
            And: 201 상태, 지하철 노선 정보, 관련 역 정보, 저장 경로를 응답 받는다.
     */
    @Test
    @DisplayName("지하철 노선을 등록한다.")
    void create() {
        // given
        final Long upStationId = createStation(HYEHWA);
        final Long downStationId = createStation(SUNGSHIN);

        final Map<String, Object> params = new HashMap<>();
        params.put("name", LINE_4);
        params.put("color", SKY_BLUE);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);
        params.put("extraFare", 900);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
        final List<StationResponse> stationResponses = response.body().jsonPath()
                .getList("stations", StationResponse.class);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.header("Location")).isNotBlank(),
            () -> assertThat(response.body().jsonPath().getString("name")).isEqualTo(LINE_4),
            () -> assertThat(response.body().jsonPath().getString("color")).isEqualTo(SKY_BLUE),
            () -> assertThat(response.body().jsonPath().getInt("extraFare")).isEqualTo(900),
            () -> assertThat(stationResponses).hasSize(2),
            () -> assertThat(stationResponses.get(0).getName()).isEqualTo(HYEHWA),
            () -> assertThat(stationResponses.get(1).getName()).isEqualTo(SUNGSHIN)
        );
    }

    /*
        Scenario: 중복된 지하철 노선 등록
            When: 지하철 노선 등록을 요청한다.
            Then: 같은 이름의 지하철 노선 등록을 요청한다.
            And: 400 상태, 에러 메시지를 응답 받는다.
     */
    @Test
    @DisplayName("기존에 존재하는 노선 이름으로 생성하면, 예외를 발생한다.")
    void createWithDuplicateName() {
        // given
        final Long upStationId = createStation(HYEHWA);
        final Long downStationId = createStation(SUNGSHIN);
        createLine(LINE_4, SKY_BLUE, upStationId, downStationId, 10, 900);

        final Map<String, Object> params = new HashMap<>();
        params.put("name", LINE_4);
        params.put("color", SKY_BLUE);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
        Scenario: 지하럴 노선 목록 조회
            When: 지하철 노선 목록 조회를 요청한다.
            Then: 200 상태, 모든 지하철 역 정보, 관련 역 종보를 응답 받는다.
     */
    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void showAll() {
        // given
        final Long stationId1 = createStation(HYEHWA);
        final Long stationId2 = createStation(SUNGSHIN);
        final Long stationId3 = createStation(GANGNAM);
        final Long stationId4 = createStation(JAMSIL);
        createLine(LINE_4, SKY_BLUE, stationId1, stationId2, 10, 900);
        createLine(LINE_2, GREEN, stationId3, stationId4, 10, 0);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
        final List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(lineResponses.get(0).getName()).isEqualTo(LINE_4);
            assertThat(lineResponses.get(0).getColor()).isEqualTo(SKY_BLUE);
            assertThat(lineResponses.get(0).getExtraFare()).isEqualTo(900);
            assertThat(lineResponses.get(0).getStations().get(0).getName()).isEqualTo(HYEHWA);
            assertThat(lineResponses.get(0).getStations().get(1).getName()).isEqualTo(SUNGSHIN);
            assertThat(lineResponses.get(1).getName()).isEqualTo(LINE_2);
            assertThat(lineResponses.get(1).getColor()).isEqualTo(GREEN);
            assertThat(lineResponses.get(1).getExtraFare()).isEqualTo(0);
            assertThat(lineResponses.get(1).getStations().get(0).getName()).isEqualTo(GANGNAM);
            assertThat(lineResponses.get(1).getStations().get(1).getName()).isEqualTo(JAMSIL);
        });
    }

    /*
        Scenario: 지하럴 노선 조회
            When: 지하철 노선 조회를 요청한다.
            Then: 200 상태, 지하철 역 정보, 관련 역 종보를 응답 받는다.
     */
    @Test
    @DisplayName("지하철 노선 ID로 노선을 조회한다.")
    void show() {
        // given
        final Long upStationId = createStation(HYEHWA);
        final Long downStationId = createStation(SUNGSHIN);
        final Long lineId = createLine(LINE_4, SKY_BLUE, upStationId, downStationId, 10, 900);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .extract();
        final List<StationResponse> stationResponses = response.body().jsonPath()
                .getList("stations", StationResponse.class);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.body().jsonPath().getString("name")).isEqualTo(LINE_4);
            assertThat(response.body().jsonPath().getString("color")).isEqualTo(SKY_BLUE);
            assertThat(response.body().jsonPath().getInt("extraFare")).isEqualTo(900);
            assertThat(stationResponses).hasSize(2);
            assertThat(stationResponses.get(0).getName()).isEqualTo(HYEHWA);
            assertThat(stationResponses.get(1).getName()).isEqualTo(SUNGSHIN);
        });
    }

    /*
        Scenario: 없는 지하철 노선 조회
            When: 없는 지하철 노선 조회를 요청한다.
            Then: 404 상태, 에러 메시지를 응답 받는다.
     */
    @Test
    @DisplayName("존재하지 않는 지하철 노선 ID로 조회한다면, 예외를 발생한다.")
    void getLineNotExistId() {
        // given
        final long id = 1L;

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /*
        Scenario: 지하철 노선 수정
            When: 지하철 노선 수정을 요청한다.
            Then: 지하철 노선이 수정된다.
            And: 200 상태를 응답한다.
     */
    @Test
    @DisplayName("노선을 업데이트 한다.")
    void update() {
        // given
        final Long upStationId = createStation(HYEHWA);
        final Long downStationId = createStation(SUNGSHIN);

        final Long lineId = createLine(LINE_4, SKY_BLUE, upStationId, downStationId, 10, 900);

        final Map<String, Object> params = new HashMap<>();
        params.put("name", LINE_2);
        params.put("color", GREEN);
        params.put("extraFare", 0);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /*
        Scenario: 없는 지하철 노선 수정
            When: 없는 지하철 노선 수정을 요청한다.
            Then: 404 상태, 에러 메시지를 응답 받는다.
     */
    @Test
    @DisplayName("존재하지 않는 ID로 업데이트 한다면, 예외를 발생한다.")
    void updateNotExistId() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("name", LINE_4);
        params.put("color", SKY_BLUE);
        params.put("extraFare", 0);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + 100L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /*
        Scenario: 지하철 노선 제거
            When: 지하철 노선 제거를 요청한다.
            Then: 지하철 노선이 제거된다.
            And: 204 상태를 응답 받는다.
     */
    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void delete() {
        // given
        final Long upStationId = createStation(HYEHWA);
        final Long downStationId = createStation(SUNGSHIN);
        final Long lineId = createLine(LINE_4, SKY_BLUE, upStationId, downStationId, 10, 900);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/lines/" + lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /*
        Scenario: 없는 지하철 노선 제거
            When: 없는 지하철 노선 제거을 요청한다.
            Then: 404 상태, 에러 메시지를 응답 받는다.
     */
    @Test
    @DisplayName("존재하지 않는 ID로 삭제한다면, 예외를 발생한다.")
    void deleteLineNotExistId() {
        // given
        final long id = 1L;

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/lines/" + id)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Nested
    @DisplayName("지하철 구간을 등록한다.")
    class CreateSectionTest extends AcceptanceTest {

        /*
            Scenario: 지하철 구간 등록
                When: 지하철 구간 등록을 요청한다.
                Then: 지하철 구간이 생성된다.
                And: 200 상태를 응답 받는다.
         */
        @Test
        @DisplayName("노선의 끝에 구간을 추가한다. - 성공 200")
        void createSection1() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId2, 10, 0);

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId2);
            params.put("downStationId", stationId3);
            params.put("distance", 10);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("노선의 처음에 구간을 추가한다. - 성공 200")
        void createSection2() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId2, stationId3, 10, 0);

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId1);
            params.put("downStationId", stationId2);
            params.put("distance", 10);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("상행역이 겹치는 구간을 등록한다. - 성공 200")
        void createSection3() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId3, 10, 0);

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId1);
            params.put("downStationId", stationId2);
            params.put("distance", 7);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("하행역이 겹치는 구간을 등록한다. - 성공 200")
        void createSection4() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId3, 10, 0);

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId2);
            params.put("downStationId", stationId3);
            params.put("distance", 1);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("상/하행역이 겹치는 구간을 등록할 때, 기존보다 긴 구간을 등록한다. - 실패 400")
        void createSection5() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId3, 10, 0);

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId1);
            params.put("downStationId", stationId2);
            params.put("distance", 15);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("라인이 없는 경우 - 실패 404")
        void createSection6() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final long lineId = 10L;

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId1);
            params.put("downStationId", stationId2);
            params.put("distance", 15);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("역이 없는 경우 - 실패 404")
        void createSection7() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId2, 10, 0);

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId2);
            params.put("downStationId", 3L);
            params.put("distance", 15);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("연결되지 않은 구간일 경우 - 실패 400")
        void createSection8() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long stationId4 = createStation(JAMSIL);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId2, 10, 0);

            final Map<String, Object> params = new HashMap<>();
            params.put("upStationId", stationId3);
            params.put("downStationId", stationId4);
            params.put("distance", 15);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/lines/" + lineId + "/sections")
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    @Nested
    @DisplayName("지하철 구간을 삭제한다.")
    class DeleteSectionTest extends AcceptanceTest {

        /*
            Scenario: 지하철 구간 제거
                When: 지하철 구간 제거를 요청한다.
                Then: 지하철 구간이 제거된다.
                And: 200 상태를 응답 받는다.
         */
        @Test
        @DisplayName("구간을 삭제한다. - 성공 200")
        void deleteSection1() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId2, 10, 0);
            createSection(lineId, stationId2, stationId3, 10);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .when()
                    .delete("/lines/" + lineId + "/sections?stationId=" + stationId1)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        @DisplayName("없는 라인의 구간을 삭제한다. - 실패 404")
        void deleteSections2() {
            // given
            final Long stationId = createStation(HYEHWA);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .when()
                    .delete("/lines/" + 10L + "/sections?stationId=" + stationId)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("없는 역의 구간을 삭제한다. - 실패 404")
        void deleteSections3() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long stationId3 = createStation(GANGNAM);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId2, 10, 0);
            createSection(lineId, stationId2, stationId3, 10);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .when()
                    .delete("/lines/" + lineId + "/sections?stationId=" + 10L)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("삭제할 수 없는 구간을 삭제한다. - 실패 400")
        void deleteSections4() {
            // given
            final Long stationId1 = createStation(HYEHWA);
            final Long stationId2 = createStation(SUNGSHIN);
            final Long lineId = createLine(LINE_4, SKY_BLUE, stationId1, stationId2, 10, 0);

            // when
            final ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .when()
                    .delete("/lines/" + lineId + "/sections?stationId=" + stationId1)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
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
                            final int distance, final int extraFare) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("extraFare", extraFare);

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
