package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.ui.dto.ExceptionResponse;
import wooteco.subway.ui.dto.LineResponse;
import wooteco.subway.ui.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";
    private static final Integer LINE_DISTANCE = 10;
    private static final Integer LINE_EXTRA_FARE = 200;

    private StationResponse stationResponse1;
    private StationResponse stationResponse2;

    @BeforeEach
    void setUpLineAcceptanceTest() {
        stationResponse1 = requestToCreateStation("강남역").as(StationResponse.class);
        stationResponse2 = requestToCreateStation("역삼역").as(StationResponse.class);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = requestToCreateLine(
                LINE_NAME, LINE_COLOR, stationResponse1.getId(),
                stationResponse2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);
        LineResponse lineResponse = response.as(LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(lineResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("green"),
                () -> assertThat(lineResponse.getExtraFare()).isEqualTo(200),
                () -> assertThat(stationResponses.get(0).getId()).isEqualTo(stationResponse1.getId()),
                () -> assertThat(stationResponses.get(0).getName()).isEqualTo(stationResponse1.getName()),
                () -> assertThat(stationResponses.get(1).getId()).isEqualTo(stationResponse2.getId()),
                () -> assertThat(stationResponses.get(1).getName()).isEqualTo(stationResponse2.getName())
        );
    }

    @DisplayName("기존에 존재하는 노선의 이름으로 노선을 생성하면 badRequest를 응답한다.")
    @Test
    void createLine_badRequest_DuplicateName() {
        // given
        requestToCreateLine(LINE_NAME, LINE_COLOR, stationResponse1.getId(), stationResponse2.getId(),
                LINE_DISTANCE, LINE_EXTRA_FARE);

        // when
        ExtractableResponse<Response> response = requestToCreateLine(LINE_NAME, LINE_COLOR,
                stationResponse1.getId(), stationResponse2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("이름은 중복될 수 없습니다.")
        );
    }

    @DisplayName("노선을 생성하는데 필요한 정보 중 null 혹은 빈 문자열이 하나라도 존재할 경우에 badRequest를 응답한다.")
    @ParameterizedTest
    @MethodSource("provideNotInvalidCreationSource")
    void createLine_badRequest_InvalidResource(String name, String color, Long upStationId, Long downStationId,
                                               Integer distance, Integer extraFare) {
        ExtractableResponse<Response> response =
                requestToCreateLine(name, color, upStationId, downStationId, distance, extraFare);
        ExceptionResponse exceptionResponse = response.jsonPath()
                .getObject(".", ExceptionResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("입력되지 않은 정보가 있습니다.")
        );
    }

    @DisplayName("노선을 생성하는데 상행역과 하행역의 거리가 1 미만이라면 badRequest를 응답한다.")
    @Test
    void createLine_badRequest_lowerThanOneDistance() {
        // given
        Integer invalidDistance = 0;

        // when
        ExtractableResponse<Response> response =
                requestToCreateLine(LINE_NAME, LINE_COLOR,
                        stationResponse1.getId(), stationResponse2.getId(), invalidDistance, LINE_EXTRA_FARE);
        ExceptionResponse exceptionResponse = response.jsonPath()
                .getObject(".", ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("역간의 거리는 1 이상이어야 합니다.")
        );
    }

    @DisplayName("노선 추가 요금이 100원 미만이라면 badRequest를 응답한다.")
    @Test
    void create_badRequest_extraFareLowerThanMinValue() {
        //given
        Integer invalidExtraFare = 99;

        //when
        ExtractableResponse<Response> response = requestToCreateLine(LINE_NAME, LINE_COLOR,
                stationResponse1.getId(), stationResponse2.getId(), LINE_DISTANCE, invalidExtraFare);
        ExceptionResponse exceptionResponse = response.jsonPath()
                .getObject(".", ExceptionResponse.class);

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("노선의 최소 추가 금액은 100원입니다.")
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllLines() {
        /// given
        StationResponse station3 = requestToCreateStation("교대역").as(StationResponse.class);
        StationResponse station4 = requestToCreateStation("수서역").as(StationResponse.class);

        requestToCreateLine(LINE_NAME, LINE_COLOR, stationResponse1.getId(), stationResponse2.getId(), LINE_DISTANCE,
                LINE_EXTRA_FARE);
        requestToCreateLine("3호선", "orange", station3.getId(), station4.getId(), 10, 300);

        // when
        ExtractableResponse<Response> response = requestToFindAllLines();
        List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);

        LineResponse lineResponse1 = lineResponses.get(0);
        List<StationResponse> stationResponses1 = lineResponse1.getStations();
        LineResponse lineResponse2 = lineResponses.get(1);
        List<StationResponse> stationResponses2 = lineResponse2.getStations();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse1.getId()).isEqualTo(1L),
                () -> assertThat(lineResponse1.getName()).isEqualTo("2호선"),
                () -> assertThat(lineResponse1.getColor()).isEqualTo("green"),
                () -> assertThat(lineResponse1.getExtraFare()).isEqualTo(200),
                () -> assertThat(stationResponses1.get(0).getId()).isEqualTo(stationResponse1.getId()),
                () -> assertThat(stationResponses1.get(0).getName()).isEqualTo(stationResponse1.getName()),
                () -> assertThat(stationResponses1.get(1).getId()).isEqualTo(stationResponse2.getId()),
                () -> assertThat(stationResponses1.get(1).getName()).isEqualTo(stationResponse2.getName()),

                () -> assertThat(lineResponse2.getId()).isEqualTo(2L),
                () -> assertThat(lineResponse2.getName()).isEqualTo("3호선"),
                () -> assertThat(lineResponse2.getColor()).isEqualTo("orange"),
                () -> assertThat(lineResponse2.getExtraFare()).isEqualTo(300),
                () -> assertThat(stationResponses2.get(0).getId()).isEqualTo(station3.getId()),
                () -> assertThat(stationResponses2.get(0).getName()).isEqualTo(station3.getName()),
                () -> assertThat(stationResponses2.get(1).getId()).isEqualTo(station4.getId()),
                () -> assertThat(stationResponses2.get(1).getName()).isEqualTo(station4.getName())
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLineById() {
        /// given
        ExtractableResponse<Response> createResponse = requestToCreateLine(
                LINE_NAME, LINE_COLOR, stationResponse1.getId(), stationResponse2.getId(), LINE_DISTANCE,
                LINE_EXTRA_FARE);

        // when
        long expectedLineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = requestToFindLineById(expectedLineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("green"),
                () -> assertThat(lineResponse.getExtraFare()).isEqualTo(200),
                () -> assertThat(stationResponses.get(0).getId()).isEqualTo(stationResponse1.getId()),
                () -> assertThat(stationResponses.get(0).getName()).isEqualTo(stationResponse1.getName()),
                () -> assertThat(stationResponses.get(1).getId()).isEqualTo(stationResponse2.getId()),
                () -> assertThat(stationResponses.get(1).getName()).isEqualTo(stationResponse2.getName())
        );
    }

    @DisplayName("조회할 지하철 노선이 없는 경우 NOT FOUND를 응답한다.")
    @Test
    void findLineById_NotFound() {
        // when
        ExtractableResponse<Response> response = requestToFindLineById(1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> response = requestToCreateLine(
                LINE_NAME, LINE_COLOR, stationResponse1.getId(), stationResponse2.getId(), LINE_DISTANCE,
                LINE_EXTRA_FARE);
        long savedLineId = Long.parseLong(response.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> updateResponse =
                requestToUpdateLine(savedLineId, "3호선", "orange", 500);
        LineResponse lineResponse = requestToFindLineById(savedLineId).as(LineResponse.class);

        // then
        assertAll(
                () -> assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse.getName()).isEqualTo("3호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("orange"),
                () -> assertThat(lineResponse.getExtraFare()).isEqualTo(500)
        );
    }

    @DisplayName("노선을 수정하는데 필요한 정보 중 하나라도 유효하지 않을 시에 badRequest를 응답한다")
    @ParameterizedTest
    @MethodSource("provideInvalidLineModificationResource")
    void updateLine_badRequest_InvalidResource(String name, String color, Integer extraFare) {
        // given
        ExtractableResponse<Response> lineCreationResponse = requestToCreateLine(
                LINE_NAME, LINE_COLOR, stationResponse1.getId(), stationResponse2.getId(), LINE_DISTANCE,
                LINE_EXTRA_FARE);
        long savedLineId = Long.parseLong(lineCreationResponse.header("Location").split("/")[2]);

        //when
        ExtractableResponse<Response> response =
                requestToUpdateLine(savedLineId, name, color, extraFare);
        ExceptionResponse exceptionResponse = response.jsonPath().getObject(".", ExceptionResponse.class);

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("입력되지 않은 정보가 있습니다.")
        );
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLineById() {
        // given
        LineResponse lineResponse = requestToCreateLine(
                LINE_NAME, LINE_COLOR, stationResponse1.getId(), stationResponse2.getId(), LINE_DISTANCE,
                LINE_EXTRA_FARE)
                .as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = requestToDeleteLineById(lineResponse.getId());
        List<LineResponse> findingAllResponse = requestToFindAllLines().jsonPath()
                .getList(".", LineResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(findingAllResponse).isEmpty()
        );
    }

    private ExtractableResponse<Response> requestToFindAllLines() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestToUpdateLine(long savedLineId, String name, String color,
                                                              Integer extraFare) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("extraFare", extraFare);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + savedLineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestToDeleteLineById(Long lindId) {
        return RestAssured.given().log().all()
                .when()
                .delete("/lines/" + lindId)
                .then().log().all()
                .extract();
    }


    private static Stream<Arguments> provideNotInvalidCreationSource() {
        return Stream.of(
                Arguments.of(null, LINE_COLOR, 1L, 2L, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of("", LINE_COLOR, 1L, 2L, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of(" ", LINE_COLOR, 1L, 2L, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, null, 1L, 2L, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, "", 1L, 2L, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, " ", 1L, 2L, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, LINE_COLOR, null, 2L, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, LINE_COLOR, 1L, null, LINE_DISTANCE, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, LINE_COLOR, 1L, 2L, null, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, LINE_COLOR, 1L, 2L, LINE_DISTANCE, null)
        );
    }

    private static Stream<Arguments> provideInvalidLineModificationResource() {
        return Stream.of(
                Arguments.of(null, LINE_COLOR, LINE_EXTRA_FARE),
                Arguments.of("", LINE_COLOR, LINE_EXTRA_FARE),
                Arguments.of(" ", LINE_COLOR, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, null, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, "", LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, " ", LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, LINE_COLOR, null)
        );
    }
}
