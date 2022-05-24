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
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.ExceptionResponse;
import wooteco.subway.ui.dto.LineResponse;
import wooteco.subway.ui.dto.StationRequest;
import wooteco.subway.ui.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private StationResponse station1;
    private StationResponse station2;
    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";
    private static final Integer LINE_DISTANCE = 10;
    private static final Integer LINE_EXTRA_FARE = 200;

    @BeforeEach
    void setUpLineAcceptanceTest() {
        station1 = createStation(new StationRequest("강남역")).as(StationResponse.class);
        station2 = createStation(new StationRequest("역삼역")).as(StationResponse.class);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = createLine(
                LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);
        LineResponse lineResponse = response.as(LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(lineResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("green"),
                () -> assertThat(lineResponse.getExtraFare()).isEqualTo(200),
                () -> assertThat(stationResponses.get(0).getId()).isEqualTo(station1.getId()),
                () -> assertThat(stationResponses.get(0).getName()).isEqualTo(station1.getName()),
                () -> assertThat(stationResponses.get(1).getId()).isEqualTo(station2.getId()),
                () -> assertThat(stationResponses.get(1).getName()).isEqualTo(station2.getName())
        );
    }

    @DisplayName("기존에 존재하는 노선의 이름으로 노선을 생성하면 badRequest를 응답한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        createLine(LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);

        // when
        ExtractableResponse<Response> response = createLine(
                LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("이름은 중복될 수 없습니다.")
        );
    }

    @DisplayName("노선을 생성하는데 필요한 정보 중 하나라도 유효하지 않을 시에 badRequest를 응답한다.")
    @ParameterizedTest
    @MethodSource("provideNotInvalidCreationSource")
    void createLineWithInvalidResource(String name, String color, Long upStationId, Long downStationId,
                                       Integer distance, Integer extraFare) {
        ExtractableResponse<Response> response =
                createLine(name, color, upStationId, downStationId, distance, extraFare);
        ExceptionResponse exceptionResponse = response.jsonPath().getObject(".", ExceptionResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).contains("이(가) 유효하지 않습니다.")
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getAllLines() {
        /// given
        Station station3 = createStation(new StationRequest("교대역")).as(Station.class);
        Station station4 = createStation(new StationRequest("수서역")).as(Station.class);

        createLine(LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);
        createLine("3호선", "orange", station3.getId(), station4.getId(), 10, 300);

        // when
        ExtractableResponse<Response> response = findAllLines();
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
                () -> assertThat(stationResponses1.get(0).getId()).isEqualTo(station1.getId()),
                () -> assertThat(stationResponses1.get(0).getName()).isEqualTo(station1.getName()),
                () -> assertThat(stationResponses1.get(1).getId()).isEqualTo(station2.getId()),
                () -> assertThat(stationResponses1.get(1).getName()).isEqualTo(station2.getName()),

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
    void getLine() {
        /// given
        ExtractableResponse<Response> createResponse = createLine(
                LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);

        // when
        long expectedLineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/" + expectedLineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse.getName()).isEqualTo("2호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("green"),
                () -> assertThat(lineResponse.getExtraFare()).isEqualTo(200),
                () -> assertThat(stationResponses.get(0).getId()).isEqualTo(station1.getId()),
                () -> assertThat(stationResponses.get(0).getName()).isEqualTo(station1.getName()),
                () -> assertThat(stationResponses.get(1).getId()).isEqualTo(station2.getId()),
                () -> assertThat(stationResponses.get(1).getName()).isEqualTo(station2.getName())
        );
    }

    @DisplayName("조회할 지하철 노선이 없는 경우 NOT FOUND를 응답한다.")
    @Test
    void getNotExistLine() {
        // when
        ExtractableResponse<Response> response = findLineById(1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> response = createLine(
                LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);
        long savedLineId = Long.parseLong(response.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> updateResponse =
                updateLine(savedLineId, "3호선", "orange", 500);
        LineResponse lineResponse = findLineById(savedLineId).as(LineResponse.class);

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
    void modifyLineWithInvalidResource(String name, String color, Integer extraFare) {
        // given
        ExtractableResponse<Response> lineCreationResponse = createLine(
                LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE);
        long savedLineId = Long.parseLong(lineCreationResponse.header("Location").split("/")[2]);

        //when
        ExtractableResponse<Response> response =
                updateLine(savedLineId, name, color, extraFare);
        ExceptionResponse exceptionResponse = response.jsonPath().getObject(".", ExceptionResponse.class);

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).contains("이(가) 유효하지 않습니다.")
        );
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = createLine(
                LINE_NAME, LINE_COLOR, station1.getId(), station2.getId(), LINE_DISTANCE, LINE_EXTRA_FARE)
                .as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = deleteById(lineResponse.getId());
        List<LineResponse> findingAllResponse = findAllLines().jsonPath().getList(".", LineResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(findingAllResponse).isEmpty()
        );
    }

    private ExtractableResponse<Response> findAllLines() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> updateLine(long savedLineId, String name, String color, Integer extraFare) {
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

    private ExtractableResponse<Response> deleteById(Long lindId) {
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
                Arguments.of(LINE_NAME, LINE_COLOR, 1L, 2L, 0, LINE_EXTRA_FARE),
                Arguments.of(LINE_NAME, LINE_COLOR, 1L, 2L, LINE_DISTANCE, null),
                Arguments.of(LINE_NAME, LINE_COLOR, 1L, 2L, LINE_DISTANCE, 0)
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
                Arguments.of(LINE_NAME, LINE_COLOR, null),
                Arguments.of(LINE_NAME, LINE_COLOR, 0)
        );
    }
}
