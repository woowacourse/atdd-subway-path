package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.delete;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.get;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.post;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.put;
import static wooteco.subway.acceptance.fixture.TestLine.LINE_2;
import static wooteco.subway.acceptance.fixture.TestLine.LINE_9;
import static wooteco.subway.acceptance.fixture.TestStation.GANGNAM;
import static wooteco.subway.acceptance.fixture.TestStation.YEOKSAM;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.LineUpdateRequest;
import wooteco.subway.dto.response.LineResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("올바른 생성 요청으로 지하철 노선을 생성하면 201 CREATED와 노선 리소스 주소를 반환한다.")
    @Test
    void createLine() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();

        ExtractableResponse<Response> response = LINE_2.requestSave(gangnam, yeoksam, 1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선 생성을 요청하면 400 BAD REQUEST를 반환한다.")
    @Test
    void createLineWithDuplicateName() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        LINE_2.requestSave(gangnam, yeoksam, 1);

        LineCreateRequest invalidRequest = new LineCreateRequest("2호선", "분홍색", gangnam.getId(), yeoksam.getId(), 1, 0);
        ExtractableResponse<Response> response = post(invalidRequest, "/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("이미 존재하는 노선 이름입니다.");
    }

    @DisplayName("기존에 존재하는 노선 색상으로 노선 생성을 요청하면 400 BAD REQUEST를 반환한다.")
    @Test
    void createLineWithDuplicateColor() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        LINE_2.requestSave(gangnam, yeoksam, 1);

        LineCreateRequest invalidRequest = new LineCreateRequest("성수지선", "초록색", gangnam.getId(), yeoksam.getId(), 1, 0);
        ExtractableResponse<Response> response = post(invalidRequest, "/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("이미 존재하는 노선 색상입니다.");
    }

    @DisplayName("전체 노선 조회를 요청하면 등록되어 있는 2호선과 3호선 정보를 조회하고 200 OK를 반환한다.")
    @Test
    void getLines() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        Line line9 = LINE_9.save(gangnam, yeoksam, 1);

        ExtractableResponse<Response> response = get("/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> lineResponses = response.body().jsonPath().getList(".", LineResponse.class);

        assertThat(lineResponses).usingRecursiveComparison()
                .isEqualTo(List.of(
                        new LineResponse(line2, List.of(gangnam, yeoksam)),
                        new LineResponse(line9, List.of(gangnam, yeoksam))
                ));
    }

    @DisplayName("등록되어 있는 노선의 id를 URI에 담아서 조회를 요청하면 해당 노선의 정보와 200 OK를 반환한다.")
    @Test
    void getLine() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);

        ExtractableResponse<Response> response = get("/lines/" + line2.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new LineResponse(line2, List.of(gangnam, yeoksam)));
    }

    @DisplayName("등록되어 있는 노선의 id를 URI에 담아서 수정 요청을 하면 수정된 노선 정보와 200 OK를 반환한다.")
    @Test
    void updateLine() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("1호선", "군청색", 0);
        put(lineUpdateRequest, "/lines/" + line2.getId());
        Line line1 = new Line(line2.getId(), lineUpdateRequest.getName(), lineUpdateRequest.getColor(),
                lineUpdateRequest.getExtraFare());

        ExtractableResponse<Response> response = get("/lines/" + line2.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new LineResponse(line1, List.of(gangnam, yeoksam)));
    }

    @DisplayName("등록되어있는 노선의 id를 URI에 담아서 삭제 요청을 하면 204 NO CONTENT를 반환한다.")
    @Test
    void deleteLine() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        Line line2 = LINE_2.save(gangnam, yeoksam, 1);

        ExtractableResponse<Response> response = delete("/lines/" + line2.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선 이름이나 색으로 null 또는 공백을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {",", "'',''", "' ',' '"})
    void notAllowNullOrBlankNameAndColor(String name, String color) {
        Long gangnamId = GANGNAM.save().getId();
        Long yeoksamId = YEOKSAM.save().getId();
        LineCreateRequest lineCreateRequest = new LineCreateRequest(name, color, gangnamId, yeoksamId, 1, 0);

        ExtractableResponse<Response> response = post(lineCreateRequest, "/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("빈 값일 수 없습니다.");
    }

    @DisplayName("시점 또는 종점 id로 null 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowNullStationId() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", null, null, 1, 0);

        ExtractableResponse<Response> response = post(lineCreateRequest, "/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("id는 null일 수 없습니다.");
    }

    @DisplayName("시점 또는 종점 id로 1보다 작은 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowStationIdLessThan1() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", 0L, 0L, 1, 0);

        ExtractableResponse<Response> response = post(lineCreateRequest, "/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("id는 1보다 작을 수 없습니다.");
    }

    @DisplayName("노선 거리로 1보다 작은 값을 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowDistanceLessThan1() {
        Long gangnamId = GANGNAM.save().getId();
        Long yeoksamId = YEOKSAM.save().getId();
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 0, 0);

        ExtractableResponse<Response> response = post(lineCreateRequest, "/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("노선 거리는 1보다 작을 수 없습니다.");
    }

    @DisplayName("추가 요금으로 음수를 넣어서 생성 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowExtraFareLessThan0() {
        Long gangnamId = GANGNAM.save().getId();
        Long yeoksamId = YEOKSAM.save().getId();
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "초록색", gangnamId, yeoksamId, 1, -1);

        ExtractableResponse<Response> response = post(lineCreateRequest, "/lines");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("추가 요금은 음수일 수 없습니다.");
    }

    @DisplayName("노선 요청 시 인수 타입이 맞지 않으면 400 Bad Request를 반환한다.")
    @Test
    void unSupportedMethodArgumentsType() {
        ExtractableResponse<Response> response = get("/lines/잘못됨");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
