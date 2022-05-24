package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.BodyCreator.makeBodyForPost;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.controller.response.LineResponse;
import wooteco.subway.dto.controller.response.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        createStation("강남역");
        createStation("선릉역");

        // when
        ExtractableResponse<Response> response = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("노선 저장 시 요청 객체의 요청 정보를 누락(공백, null)하면 에러를 응답한다.")
    @Test
    void createLineMissingParam() {
        // given

        // when
        ExtractableResponse<Response> response = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("", "", "", "", "", ""),
                "/lines"
        );

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString())
                        .contains("name", "color", "upStationId", "downStationId", "distance", "extraFare")
        );
    }

    @DisplayName("이름의 길이가 256 이상이면 노선을 만들 수 없다.")
    @Test
    void createLineWithLongName() {
        // given
        createStation("강남역");
        createStation("선릉역");

        // when
        ExtractableResponse<Response> response = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("a".repeat(256), "green", "1", "2", "10", "900"),
                "/lines"
        );

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).contains("존재할 수 없는 이름입니다.")
        );
    }

    @DisplayName("노선 색의 길이가 21 이상이면 노선을 만들 수 없다.")
    @Test
    void createLineWithLongColor() {
        // given
        createStation("강남역");
        createStation("선릉역");

        // when
        ExtractableResponse<Response> response = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "a".repeat(21), "1", "2", "10", "900"),
                "/lines"
        );

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).contains("존재할 수 없는 색상입니다.")
        );
    }

    @DisplayName("거리가 1 이상이 아닌 경우 지하철 노선을 생성할 수 없다")
    @Test
    void createLineWithWrongDistance() {
        // given
        createStation("강남역");
        createStation("선릉역");

        // when
        ExtractableResponse<Response> response = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "-1", "900"),
                "/lines"
        );

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).contains("구간 사이의 거리는 양수여야합니다.")
        );
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.(400에러)")
    @Test
    void createLineWithDuplicateName() {
        // given
        createStation("강남역");
        createStation("선릉역");

        RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선의 구간에 맞는 역이 없다면 지하철 노선을 생성할 수 없다.(404에러)")
    @Test
    void createLineNoStation() {
        // given

        // when
        ExtractableResponse<Response> response = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선 전체를 조회한다.")
    @Test
    void getLines() {
        /// given
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createResponse1 = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> createResponse2 = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("3호선", "blue", "1", "2", "10", "900"),
                "/lines"
        );

        // when
        ExtractableResponse<Response> response = RequestFrame.get("/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        /// given
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createResponse1 = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        // when
        String uri = createResponse1.header("Location");
        ExtractableResponse<Response> response = RequestFrame.get(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Long expectedLineId = Long.parseLong(createResponse1.header("Location").split("/")[2]);
        assertThat(expectedLineId).isEqualTo(response.jsonPath().getLong("id"));
        assertThat(createResponse1.jsonPath().getLong("id")).isEqualTo(response.jsonPath().getLong("id"));
        assertThat(createResponse1.jsonPath().getString("name")).isEqualTo(response.jsonPath().getString("name"));
        assertThat(createResponse1.jsonPath().getString("color")).isEqualTo(response.jsonPath().getString("color"));
    }

    @DisplayName("지하철 노선의 역은 상행선부터 정렬되어서 응답한다.")
    @Test
    void getLineCheckSort() {
        /// given
        createStation("신림역");
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");
        createStation("왕십리역");

        ExtractableResponse<Response> createResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "5", "4", "10", "900"),
                "/lines"
        );
        long id = createResponse.jsonPath().getLong("id");
        RequestFrame.post(
                makeBodyForPost("4", "3", "10"),
                "/lines/" + id + "/sections"
        );
        RequestFrame.post(
                makeBodyForPost("3", "2", "10"),
                "/lines/" + id + "/sections"
        );
        RequestFrame.post(
                makeBodyForPost("2", "1", "10"),
                "/lines/" + id + "/sections"
        );

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RequestFrame.get(uri);

        // then
        List<StationResponse> stations = response.body()
                .jsonPath()
                .getList("stations", StationResponse.class);
        List<String> stationNames = stations.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationNames).containsExactly("왕십리역", "잠실역", "선릉역", "강남역", "신림역")
        );
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회한다.(404에러)")
    @Test
    void getLineNotExists() {
        // given

        // when
        ExtractableResponse<Response> response = RequestFrame.get("/lines/1");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        /// given
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createResponse1 = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );
        String uri = createResponse1.header("Location");

        // when
        ExtractableResponse<Response> response = RequestFrame.put(
                makeBodyForPut("다른분당선", "green", "1", "2", "10", "900"), uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정한다.(404에러)")
    @Test
    void updateLineNotExists() {
        // given

        // when
        ExtractableResponse<Response> response = RequestFrame.put(
                makeBodyForPut("다른분당선", "green", "1", "2", "10", "900"), "/lines/1");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 수정한다.(400에러)")
    @Test
    void updateLineWithDuplicateName() {
        /// given
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createResponse1 = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );
        String uri = createResponse1.header("Location");

        RequestFrame.post(
                BodyCreator.makeLineBodyForPost("다른분당선", "blue", "1", "2", "10", "900"),
                "/lines"
        );

        // when
        ExtractableResponse<Response> response = RequestFrame.put(
                makeBodyForPut("다른분당선", "green", "1", "2", "10", "900"), uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("다른분당선", "blue", "1", "2", "10", "900"),
                "/lines"
        );

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RequestFrame.delete(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 지하철 노선을 제거한다.(404에러)")
    @Test
    void deleteLineNotExists() {
        // given

        // when
        ExtractableResponse<Response> response = RequestFrame.delete("/lines/1");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private Map<String, String> makeBodyForPut(String name, String color, String upStationId,
                                               String downStationId, String distance, String extraFare) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("color", color);
        body.put("upStationId", upStationId);
        body.put("downStationId", downStationId);
        body.put("distance", distance);
        body.put("extraFare", extraFare);
        return body;
    }

    private void createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        RequestFrame.post(params, "/stations");
    }
}
