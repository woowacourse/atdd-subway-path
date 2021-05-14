package wooteco.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.controller.dto.request.LineRequestDto;
import wooteco.subway.controller.dto.response.LineResponseDto;
import wooteco.subway.controller.dto.response.StationResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponseDto 강남역;
    private StationResponseDto downStationRequestDto;
    private LineRequestDto lineRequestDto1;
    private LineRequestDto lineRequestDto2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역");
        downStationRequestDto = 지하철역_등록되어_있음("광교역");

        lineRequestDto1 = new LineRequestDto("신분당선", "bg-red-600", 강남역.getId(), downStationRequestDto.getId(), 10);
        lineRequestDto2 = new LineRequestDto("구신분당선", "bg-red-600", 강남역.getId(), downStationRequestDto.getId(), 15);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequestDto1);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(lineRequestDto1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequestDto1);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponseDto lineResponseDto1 = 지하철_노선_등록되어_있음(lineRequestDto1);
        LineResponseDto lineResponseDto2 = 지하철_노선_등록되어_있음(lineRequestDto2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(lineResponseDto1, lineResponseDto2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponseDto lineResponseDto = 지하철_노선_등록되어_있음(lineRequestDto1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineResponseDto);

        // then
        지하철_노선_응답됨(response, lineResponseDto);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponseDto lineResponseDto = 지하철_노선_등록되어_있음(lineRequestDto1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(lineResponseDto, lineRequestDto2);

        // then
        지하철_노선_수정됨(response);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponseDto lineResponseDto = 지하철_노선_등록되어_있음(lineRequestDto1);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(lineResponseDto);

        // then
        지하철_노선_삭제됨(response);
    }

    public static LineResponseDto 지하철_노선_등록되어_있음(String name, String color, StationResponseDto upStationResponseDto, StationResponseDto downStationResponseDto, int distance) {
        LineRequestDto lineRequestDto = new LineRequestDto(name, color, upStationResponseDto.getId(), downStationResponseDto.getId(), distance);
        return 지하철_노선_등록되어_있음(lineRequestDto);
    }

    public static LineResponseDto 지하철_노선_등록되어_있음(LineRequestDto lineRequestDto) {
        return 지하철_노선_생성_요청(lineRequestDto).as(LineResponseDto.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequestDto lineRequestDto) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequestDto)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(LineResponseDto lineResponseDto) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineResponseDto.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(LineResponseDto lineResponseDto, LineRequestDto lineRequestDto) {

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequestDto)
                .when().put("/lines/" + lineResponseDto.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(LineResponseDto lineResponseDto) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineResponseDto.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponseDto lineResponseDto) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponseDto resultLineResponseDto = response.as(LineResponseDto.class);
        assertThat(resultLineResponseDto.getId()).isEqualTo(lineResponseDto.getId());
    }

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, List<LineResponseDto> createdLineResponseDtos) {
        List<Long> expectedLineIds = createdLineResponseDtos.stream()
                .map(LineResponseDto::getId)
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponseDto.class).stream()
                .map(LineResponseDto::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
