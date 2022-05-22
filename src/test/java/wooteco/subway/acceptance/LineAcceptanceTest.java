package wooteco.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationRequest;

import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("NonAsciiCharacters")
class LineAcceptanceTest extends AcceptanceTest {

    private Long 선릉역_id;
    private Long 선정릉역_id;

    private final String basicPath = "/lines";

    @BeforeEach
    void setUpStations() {
        선릉역_id = RestAssuredConvenienceMethod.postRequestAndGetId(new StationRequest("선릉역"), "/stations");
        선정릉역_id = RestAssuredConvenienceMethod.postRequestAndGetId(new StationRequest("선정릉역"), "/stations");
    }

    @DisplayName("지하철 노선을 등록한다.")
    @Test
    void createLine() {
        LineRequest request = new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 500);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", response -> equalTo("/lines/" + response.path("id")));
    }

    @DisplayName("비어있는 이름으로 역을 생성하면 400번 코드를 반환한다.")
    @Test
    void createLineWithInvalidNameDateSize() {
        LineRequest request = new LineRequest("", "yellow", 선릉역_id, 선정릉역_id, 10, 500);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비어있는 색으로 역을 생성하면 400번 코드를 반환한다.")
    @Test
    void createLineWithInvalidColorDateSize() {
        LineRequest request = new LineRequest("분당선", "", 선릉역_id, 선정릉역_id, 10, 500);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복된 이름을 가진 지하철 노선을 등록할 때 400번 코드를 반환한다.")
    @Test
    void throwsExceptionWhenCreateDuplicatedName() {
        LineRequest request = new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 500);
        RestAssuredConvenienceMethod.postRequest(request, basicPath);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상행, 역이 같은 가진 지하철 노선을 등록할 때 400번 코드를 반환한다.")
    @Test
    void throwsExceptionWhenCreateLineWithSameUpDownStation() {
        LineRequest request = new LineRequest("분당선", "yellow", 선릉역_id, 선릉역_id, 10, 500);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("존재하지 않는 역으로 지하철 노선을 등록할 때 400번 코드를 반환한다.")
    @Test
    void throwsExceptionWhenCreateLineWithNonExistStation() {
        LineRequest request = new LineRequest("분당선", "yellow", 선릉역_id, 100L, 10, 500);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("0이하의 거리를 가진 구간으로 지하철 노선을 등록할 때 400번 코드를 반환한다.")
    @Test
    void throwsExceptionWhenCreateLineWithInvalidDistance() {
        LineRequest request = new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 0, 500);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("추가 요금이 음수로 지하철 노선을 등록할 때 400번 코드를 반환한다.")
    @Test
    void throwsExceptionWhenCreateLineWithNegativeExtraFare() {
        LineRequest request = new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 0, -500);

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        RestAssuredConvenienceMethod.postRequest(
                        new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 500), basicPath)
                .extract().jsonPath().getObject(".", LineResponse.class);
        RestAssuredConvenienceMethod.postRequest(
                        new LineRequest("신분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 600), basicPath)
                .extract().jsonPath().getObject(".", LineResponse.class);

        RestAssuredConvenienceMethod.getRequest(basicPath)
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        LineResponse createResponse = RestAssuredConvenienceMethod.postRequest(
                        new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 500), basicPath)
                .extract().jsonPath().getObject(".", LineResponse.class);

        RestAssuredConvenienceMethod.getRequest("/lines/" + createResponse.getId())
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        Long createdLineId = RestAssuredConvenienceMethod.postRequestAndGetId(
                new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 700), basicPath);
        Line request = new Line("다른분당선", "blue", 600);

        RestAssuredConvenienceMethod.putRequest(request, "/lines/" + createdLineId)
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        Long createdLineId = RestAssuredConvenienceMethod.postRequestAndGetId(
                new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 500), basicPath);

        RestAssuredConvenienceMethod.deleteRequest("/lines/" + createdLineId)
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 데이터를 삭제하려고 한다면 400번 코드를 반환한다.")
    @Test
    void deleteLineWithNotExistData() {
        RestAssuredConvenienceMethod.deleteRequest("/lines/" + 100L)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
