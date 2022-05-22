package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.ExceptionResponse;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.LineResponse;
import wooteco.subway.ui.dto.StationRequest;
import wooteco.subway.ui.dto.StationResponse;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private Station station1;
    private Station station2;
    private LineRequest createLineRequest;

    @BeforeEach
    void setUpLineAcceptanceTest() {
        station1 = createStation(new StationRequest("강남역")).as(Station.class);
        station2 = createStation(new StationRequest("역삼역")).as(Station.class);
        createLineRequest = new LineRequest(
                "2호선", "green", station1.getId(), station2.getId(), 10, 200);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = createLine(createLineRequest);
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

    @DisplayName("기존에 존재하는 노선의 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        createLine(createLineRequest);

        // when
        ExtractableResponse<Response> response = createLine(createLineRequest);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("이름은 중복될 수 없습니다.")
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getAllLines() {
        /// given
        Station station3 = createStation(new StationRequest("교대역")).as(Station.class);
        Station station4 = createStation(new StationRequest("수서역")).as(Station.class);

        LineRequest createLineRequest2 = new LineRequest(
                "3호선", "orange", station3.getId(), station4.getId(), 10, 300);

        createLine(createLineRequest);
        createLine(createLineRequest2);

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
        ExtractableResponse<Response> createResponse = createLine(createLineRequest);

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

    @DisplayName("조회할 지하철 노선이 없는 경우 예외가 발생한다.")
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
        ExtractableResponse<Response> response = createLine(createLineRequest);
        long savedLineId = Long.parseLong(response.header("Location").split("/")[2]);
        LineRequest updateRequest = new LineRequest(
                "3호선", "orange", null, null, 0, 500);

        // when
        ExtractableResponse<Response> updateResponse = updateLine(savedLineId, updateRequest);
        LineResponse lineResponse = findLineById(savedLineId).as(LineResponse.class);

        // then
        assertAll(
                () -> assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineResponse.getName()).isEqualTo(updateRequest.getName()),
                () -> assertThat(lineResponse.getColor()).isEqualTo(updateRequest.getColor()),
                () -> assertThat(lineResponse.getExtraFare()).isEqualTo(updateRequest.getExtraFare())
        );
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse lineResponse = createLine(createLineRequest).as(LineResponse.class);

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

    private ExtractableResponse<Response> updateLine(long savedLineId, LineRequest updateRequest) {
        return RestAssured.given().log().all()
                .body(updateRequest)
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
}
