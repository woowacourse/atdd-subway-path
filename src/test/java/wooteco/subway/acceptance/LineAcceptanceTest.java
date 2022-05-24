package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.LineUpdateRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private Long gangnamId;
    private Long yeoksamId;

    @BeforeEach
    void setStations() {
        gangnamId = requestPostStationAndReturnId(new StationRequest("강남역"));
        yeoksamId = requestPostStationAndReturnId(new StationRequest("역삼역"));
    }

    @DisplayName("올바른 생성 요청으로 지하철 노선을 생성하면 201 CREATED와 노선 리소스 주소를 반환한다.")
    @Test
    void createLine() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, 0);

        ExtractableResponse<Response> response = requestPostLine(lineCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).contains("/lines/");
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선 생성을 요청하면 400 BAD REQUEST를 반환한다.")
    @Test
    void createLineWithDuplicateName() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, 0);
        requestPostLine(lineCreateRequest);

        LineCreateRequest invalidRequest = new LineCreateRequest("2호선", "분홍색", gangnamId, yeoksamId, 1, 0);
        ExtractableResponse<Response> response = requestPostLine(invalidRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("이미 존재하는 노선 이름입니다.");
    }

    @DisplayName("기존에 존재하는 노선 색상으로 노선 생성을 요청하면 400 BAD REQUEST를 반환한다.")
    @Test
    void createLineWithDuplicateColor() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, 0);
        requestPostLine(lineCreateRequest);

        LineCreateRequest invalidRequest = new LineCreateRequest("성수지선", "초록색", gangnamId, yeoksamId, 1, 0);
        ExtractableResponse<Response> response = requestPostLine(invalidRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("이미 존재하는 노선 색상입니다.");
    }

    @DisplayName("전체 노선 조회를 요청하면 등록되어 있는 2호선과 3호선 정보를 조회하고 200 OK를 반환한다.")
    @Test
    void getLines() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, 0);
        ExtractableResponse<Response> createResponse = requestPostLine(lineCreateRequest);
        Long id1 = Long.parseLong(createResponse.header("Location").split("/")[2]);

        lineCreateRequest = new LineCreateRequest("3호선", "주황색", gangnamId, yeoksamId, 1, 0);
        createResponse = requestPostLine(lineCreateRequest);
        Long id2 = Long.parseLong(createResponse.header("Location").split("/")[2]);

        ExtractableResponse<Response> response = requestGetLines();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> lineResponses = response.body().jsonPath().getList(".", LineResponse.class);
        assertThat(lineResponses).hasSize(2)
                .extracting(LineResponse::getId, LineResponse::getName, LineResponse::getColor)
                .contains(
                        tuple(id1, "2호선", "초록색"),
                        tuple(id2, "3호선", "주황색")
                );
    }

    @DisplayName("등록되어 있는 노선의 id를 URI에 담아서 조회를 요청하면 해당 노선의 정보와 200 OK를 반환한다.")
    @Test
    void getLine() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, 0);
        ExtractableResponse<Response> createResponse = requestPostLine(lineCreateRequest);

        Long id = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = requestGetLine(id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);
        LineResponse expected = createResponse.jsonPath().getObject(".", LineResponse.class);

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("stations")
                .isEqualTo(expected);
        assertThat(actual.getStations())
                .extracting(StationResponse::getId, StationResponse::getName)
                .containsOnly(
                        tuple(gangnamId, "강남역"),
                        tuple(yeoksamId, "역삼역")
                );
    }

    @DisplayName("등록되어 있는 노선의 id를 URI에 담아서 수정 요청을 하면 수정된 노선 정보와 200 OK를 반환한다.")
    @Test
    void updateLine() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, 0);
        ExtractableResponse<Response> createResponse = requestPostLine(lineCreateRequest);

        Long id = Long.parseLong(createResponse.header("Location").split("/")[2]);

        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("1호선", "군청색", 0);
        RestAssured.given().log().all()
                .body(lineUpdateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all();

        ExtractableResponse<Response> response = requestGetLine(id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("stations")
                .isEqualTo(new LineResponse(id, "1호선", "군청색", 0, null));
        assertThat(actual.getStations())
                .extracting(StationResponse::getId, StationResponse::getName)
                .containsOnly(
                        tuple(gangnamId, "강남역"),
                        tuple(yeoksamId, "역삼역")
                );
    }

    @DisplayName("등록되어있는 노선의 id를 URI에 담아서 삭제 요청을 하면 204 NO CONTENT를 반환한다.")
    @Test
    void deleteLine() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, 0);
        ExtractableResponse<Response> createResponse = requestPostLine(lineCreateRequest);

        long id = Long.parseLong(createResponse.header("Location").split("/")[2]);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/lines/" + id)
                .then().log().all()
                .extract();

        ExtractableResponse<Response> readResponse = requestGetLines();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(readResponse.jsonPath().getList(".")).isEmpty();
    }

    @DisplayName("지하철 노선 이름이나 색으로 null 또는 공백을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {",", "'',''", "' ',' '"})
    void notAllowNullOrBlankNameAndColor(String name, String color) {
        LineCreateRequest lineCreateRequest = new LineCreateRequest(name, color, gangnamId, yeoksamId, 1, 0);
        ExtractableResponse<Response> response = requestPostLine(lineCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("빈 값일 수 없습니다.");
    }

    @DisplayName("시점 또는 종점 id로 null 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowNullStationId() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", null, null, 1, 0);

        ExtractableResponse<Response> response = requestPostLine(lineCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("id는 null일 수 없습니다.");
    }

    @DisplayName("시점 또는 종점 id로 1보다 작은 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowStationIdLessThan1() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", 0L, 0L, 1, 0);

        ExtractableResponse<Response> response = requestPostLine(lineCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("id는 1보다 작을 수 없습니다.");
    }

    @DisplayName("노선 거리로 1보다 작은 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowDistanceLessThan1() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 0, 0);

        ExtractableResponse<Response> response = requestPostLine(lineCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("노선 거리는 1보다 작을 수 없습니다.");
    }

    @DisplayName("추가 요금으로 음수를 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowExtraFareLessThan0() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 0, -1);

        ExtractableResponse<Response> response = requestPostLine(lineCreateRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("추가 요금은 음수일 수 없습니다.");
    }

    @DisplayName("노선 생성 요청 시 인수 타입이 맞지 않으면 400 Bad Request를 반환한다.")
    @Test
    void unSupportedMethodArgumentsType() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/lines/잘못됨")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
