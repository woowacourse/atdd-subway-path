package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import java.util.List;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private StationRequest stationRequest1;
    private StationRequest stationRequest2;
    private Long upStationId;
    private Long downStationId;
    private LineRequest lineRequest;
    private ExtractableResponse<Response> createResponse;

    @BeforeEach
    void setup() {
        stationRequest1 = new StationRequest("아차산역");
        stationRequest2 = new StationRequest("군자역");
        upStationId = createStation(stationRequest1);
        downStationId = createStation(stationRequest2);
        lineRequest = new LineRequest("5호선", "bg-purple-600", upStationId, downStationId, 10, 0);
        createResponse = post("/lines", lineRequest);
    }

    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        final String uri = createResponse.header("Location");

        final List<StationResponse> stations = createResponse.jsonPath().getList("stations", StationResponse.class);

        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(createResponse.header("Location")).isEqualTo(uri),
                () -> assertThat(stations).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(stationRequest1, stationRequest2))
        );
    }

    @DisplayName("노션을 조회한다.")
    @Test
    void getLines() {
        final LineRequest lineRequest2 = new LineRequest("분당선", "bg-green-600", upStationId, downStationId, 2, 0);
        final ExtractableResponse<Response> createResponse2 = post("/lines", lineRequest2);
        final LineResponse lineResponse1 = createResponse.jsonPath().getObject(".", LineResponse.class);
        final LineResponse lineResponse2 = createResponse2.jsonPath().getObject(".", LineResponse.class);
        final ExtractableResponse<Response> response = get("/lines");

        final List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);

        assertThat(lineResponses).usingRecursiveComparison()
                .isEqualTo(List.of(lineResponse1, lineResponse2));
    }

    @DisplayName("개별 노선을 ID 값으로 조회한다.")
    @Test
    void getLineById() {
        final LineResponse expected = createResponse.jsonPath().getObject(".", LineResponse.class);

        final ExtractableResponse<Response> response = get("/lines/" + expected.getId());
        final LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("노선 정보를 수정한다.")
    @Test
    void updateLineById() {
        final String expectedName = "분당선";
        final String expectedColor = "bg-yellow-600";
        final String uri = createResponse.header("Location");
        final LineRequest updateRequest = new LineRequest(expectedName, expectedColor, upStationId, downStationId, 5, 0);

        put(uri, updateRequest);
        final ExtractableResponse<Response> response = get(uri);
        final String responseName = response.jsonPath().getString("name");
        final String responseColor = response.jsonPath().getString("color");

        assertAll(
                () -> assertThat(responseName).isEqualTo(response.jsonPath().getString("name")),
                () -> assertThat(responseColor).isEqualTo(response.jsonPath().getString("color"))
        );
    }

    @DisplayName("노선을 제거한다.")
    @Test
    void deleteLine() {
        final String uri = createResponse.header("Location");

        final ExtractableResponse<Response> response = delete(uri);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철역을 생성할 경우 예외를 발생한다.")
    @Test
    void createLineWithDuplicateName() {
        final ExtractableResponse<Response> response = post("/lines", lineRequest);
        final String errorMessage = response.body().htmlPath().getString(".");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorMessage).isEqualTo("이미 존재하는 노선입니다.")
        );
    }

    private Long createStation(final StationRequest stationRequest) {
        final ExtractableResponse<Response> response = post("/stations", stationRequest);

        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
