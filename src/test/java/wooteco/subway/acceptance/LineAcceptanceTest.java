package wooteco.subway.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("노선을 등록한다.")
    void createLine() {
        // given
        final Long upStationId = extractIdByStationName("지하철역");
        final Long downStationId = extractIdByStationName("새로운지하철역");

        final LineRequest params = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10, 0);

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.post(params, "/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.as(LineResponse.class).getName()).isEqualTo("신분당선");
        assertThat(response.as(LineResponse.class).getColor()).isEqualTo("bg-red-600");
    }

    @Test
    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성한다.")
    void createLineWithDuplicateName() {
        // given
        final Long upStationId = extractIdByStationName("지하철역");
        final Long downStationId = extractIdByStationName("새로운지하철역");

        final LineRequest params = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10, 0);
        AcceptanceFixture.post(params, "/lines");

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.post(params, "/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선 전체를 조회한다.")
    void getLines() {
        // given
        final Long upStationId = extractIdByStationName("지하철역");
        final Long downStationId = extractIdByStationName("새로운지하철역");
        final Long anotherStationId = extractIdByStationName("또다른 지하철역");

        final LineRequest params1 = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10, 0);
        ExtractableResponse<Response> createResponse1 = AcceptanceFixture.post(params1, "/lines");

        final LineRequest params2 = new LineRequest("분당선", "br-green-600", upStationId, anotherStationId, 10, 0);
        ExtractableResponse<Response> createResponse2 = AcceptanceFixture.post(params2, "/lines");

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedLineIds = List.of(extractId(createResponse1), extractId(createResponse2));
        List<Long> resultLineIds = extractIds(response);
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @Test
    @DisplayName("노선 id값을 통해서 노선을 조회한다.")
    void getLine() {
        // given
        final Long upStationId = extractIdByStationName("지하철역");
        final Long downStationId = extractIdByStationName("새로운지하철역");

        final LineRequest params = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10, 0);

        ExtractableResponse<Response> param = AcceptanceFixture.post(params, "/lines");
        final String savedId = param.header("Location").split("/")[2];

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.get("/lines/" + savedId);

        // then
        final Long findId = response.response().jsonPath().getLong("id");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(Long.valueOf(savedId)).isEqualTo(findId);

        assertThat(response.as(LineResponse.class).getName()).isEqualTo("신분당선");
        assertThat(response.as(LineResponse.class).getColor()).isEqualTo("bg-red-600");
    }

    @Test
    @DisplayName("기존 노선의 이름과 색상을 변경할 수 있다.")
    void updateById() {
        // given
        final Long upStationId = extractIdByStationName("지하철역");
        final Long downStationId = extractIdByStationName("새로운지하철역");

        final LineRequest params = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10, 0);

        ExtractableResponse<Response> param = AcceptanceFixture.post(params, "/lines");
        final String savedId = param.header("Location").split("/")[2];

        // when
        Map<String, String> updateParams = new HashMap<>();
        updateParams.put("name", "다른분당선");
        updateParams.put("color", "bg-red-600");

        ExtractableResponse<Response> response = AcceptanceFixture.put(updateParams, "/lines/" + savedId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("기존 노선을 삭제할 수 있다.")
    void deleteById() {
        // given
        final Long upStationId = extractIdByStationName("지하철역");
        final Long downStationId = extractIdByStationName("새로운지하철역");

        final LineRequest params = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10, 0);

        ExtractableResponse<Response> param = AcceptanceFixture.post(params, "/lines");
        final String savedId = param.header("Location").split("/")[2];

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.delete("/lines/" + savedId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", LineResponse.class)
                .getId();
    }

    private List<Long> extractIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(toList());
    }

    private Long extractIdByStationName(String name) {
        final StationRequest stationRequest = new StationRequest(name);
        final ExtractableResponse<Response> stationResponse = AcceptanceFixture.post(stationRequest, "/stations");

        return stationResponse.jsonPath()
                .getObject(".", StationResponse.class)
                .getId();
    }
}
