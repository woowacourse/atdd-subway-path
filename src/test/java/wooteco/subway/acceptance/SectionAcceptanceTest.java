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
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.ExceptionResponse;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.LineResponse;
import wooteco.subway.ui.dto.SectionRequest;
import wooteco.subway.ui.dto.StationRequest;
import wooteco.subway.ui.dto.StationResponse;

class SectionAcceptanceTest extends AcceptanceTest {

    private StationResponse station1;
    private StationResponse station2;
    private StationResponse station3;

    @BeforeEach
    void createStation() {
        station1 = createStation(new StationRequest("강남역")).as(StationResponse.class);
        station2 = createStation(new StationRequest("역삼역")).as(StationResponse.class);
        station3 = createStation(new StationRequest("잠실역")).as(StationResponse.class);
    }

    @DisplayName("상행 종점이 같은 구간을 추가한다.")
    @Test
    void createUpperMiddleSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station3.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station2.getId(), 5);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);

        // then
        assertSectionConnection(station1, station2, station3, lineId, response);
    }

    @DisplayName("하행 종점이 같은 구간을 추가한다.")
    @Test
    void createLowerMiddleSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station3.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 5);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);

        // then
        assertSectionConnection(station1, station2, station3, lineId, response);
    }

    @DisplayName("상행 종점을 연장한다.")
    @Test
    void createUpperSection() {
        // given
        Long lineId = getCreatedLineId(station2.getId(), station3.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station2.getId(), 10);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);

        // then
        assertSectionConnection(station1, station2, station3, lineId, response);
    }

    @DisplayName("하행 종점을 연장한다.")
    @Test
    void createLowerSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);

        // then
        assertSectionConnection(station1, station2, station3, lineId, response);
    }

    @DisplayName("길이가 기존 구간의 길이를 초과한 구간을 추가할 경우 BAD_REQUEST 를 반환한다.")
    @Test
    void createLongerMiddleSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station3.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);

        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("기존 구간의 길이보다 작아야합니다.")
        );
    }

    @DisplayName("상행과 하행 종점이 모두 포함되지 않는 구간을 추가할 경우 BAD_REQUEST 를 반환한다.")
    @Test
    void createNotMatchingSection() {
        // given
        Station station4 = createStation(new StationRequest("성수역")).as(Station.class);
        Long lineId = getCreatedLineId(station1.getId(), station3.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station4.getId(), 10);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage())
                        .isEqualTo("상행 종점과 하행 종점 중 하나의 종점만 포함되어야 합니다.")
        );
    }

    @DisplayName("추가할 상행과 하행 종점이 이미 구간에 존재할 경우 BAD_REQUEST 를 반환한다.")
    @Test
    void createAllMatchingSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station2.getId(), 10);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage())
                        .isEqualTo("상행 종점과 하행 종점 중 하나의 종점만 포함되어야 합니다.")
        );
    }

    @DisplayName("상행과 하행 종점이 동일한 구간을 추가할 경우 BAD_REQUEST 를 반환한다.")
    @Test
    void createSectionWhereUpStationsIsSameAsDownStation() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station1.getId(), 10);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage())
                        .isEqualTo("상행 종점과 하행 종점 중 하나의 종점만 포함되어야 합니다.")
        );
    }

    @DisplayName("추가하려는 구간의 길이가 1 미만일 경우 BAD_REQUEST 를 반환한다.")
    @Test
    void createSectionWithInvalidDistance() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 0);
        ExtractableResponse<Response> response = createSection(lineId, sectionRequest);
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage())
                        .isEqualTo("역간의 거리는 1 이상이어야 합니다.")
        );
    }

    @DisplayName("상행 종점을 제거한다.")
    @Test
    void deleteUpperSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);
        createSection(lineId, sectionRequest);

        // when
        ExtractableResponse<Response> response = deleteSection(station1, lineId);
        LineResponse lineResponse = findLineById(lineId).as(LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationResponses).hasSize(2),
                () -> assertThat(stationResponses.get(0).getId()).isEqualTo(station2.getId()),
                () -> assertThat(stationResponses.get(0).getName()).isEqualTo(station2.getName()),
                () -> assertThat(stationResponses.get(1).getId()).isEqualTo(station3.getId()),
                () -> assertThat(stationResponses.get(1).getName()).isEqualTo(station3.getName())
        );
    }

    @DisplayName("중간역을 제거한다.")
    @Test
    void deleteMiddleSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);
        createSection(lineId, sectionRequest);

        // when
        ExtractableResponse<Response> response = deleteSection(station2, lineId);

        LineResponse lineResponse = findLineById(lineId).as(LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationResponses).hasSize(2),
                () -> assertThat(stationResponses.get(0).getId()).isEqualTo(station1.getId()),
                () -> assertThat(stationResponses.get(0).getName()).isEqualTo(station1.getName()),
                () -> assertThat(stationResponses.get(1).getId()).isEqualTo(station3.getId()),
                () -> assertThat(stationResponses.get(1).getName()).isEqualTo(station3.getName())
        );
    }

    @DisplayName("하행 종점을 제거한다.")
    @Test
    void deleteLowerSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());

        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);
        createSection(lineId, sectionRequest);

        // when
        ExtractableResponse<Response> response = deleteSection(station3, lineId);
        LineResponse lineResponse = findLineById(lineId).as(LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationResponses).hasSize(2),
                () -> assertThat(stationResponses.get(0).getId()).isEqualTo(station1.getId()),
                () -> assertThat(stationResponses.get(0).getName()).isEqualTo(station1.getName()),
                () -> assertThat(stationResponses.get(1).getId()).isEqualTo(station2.getId()),
                () -> assertThat(stationResponses.get(1).getName()).isEqualTo(station2.getName())
        );
    }

    @DisplayName("구간이 하나뿐인 노선의 구간을 삭제할 경우 BAD_REQUEST 를 반환한다.")
    @Test
    void deleteMinimumSection() {
        // given
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());

        // when
        ExtractableResponse<Response> response = deleteSection(station2, lineId);

        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(exceptionResponse.getErrorMessage()).isEqualTo("구간이 하나뿐이라 삭제할 수 없습니다.")
        );
    }

    @DisplayName("노선에 포함되지 않는 구간을 삭제할 경우 NOT_FOUND 를 반환한다.")
    @Test
    void deleteNotExistingSection() {
        // given
        StationResponse station4 = createStation(new StationRequest("성수역")).as(StationResponse.class);
        Long lineId = getCreatedLineId(station1.getId(), station2.getId());
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);
        createSection(lineId, sectionRequest);

        // when
        ExtractableResponse<Response> response = deleteSection(station4, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> deleteSection(StationResponse stationResponse, long lineId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", stationResponse.getId())
                .when()
                .delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private void assertSectionConnection(StationResponse station1, StationResponse station2, StationResponse station3,
                                         long lineId, ExtractableResponse<Response> response) {
        LineResponse lineResponse = findLineById(lineId).as(LineResponse.class);
        List<StationResponse> stationResponses = lineResponse.getStations();
        StationResponse stationResponse1 = stationResponses.get(0);
        StationResponse stationResponse2 = stationResponses.get(1);
        StationResponse stationResponse3 = stationResponses.get(2);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationResponses).hasSize(3),
                () -> assertThat(stationResponse1.getId()).isEqualTo(station1.getId()),
                () -> assertThat(stationResponse1.getName()).isEqualTo(station1.getName()),
                () -> assertThat(stationResponse2.getId()).isEqualTo(station2.getId()),
                () -> assertThat(stationResponse2.getName()).isEqualTo(station2.getName()),
                () -> assertThat(stationResponse3.getId()).isEqualTo(station3.getId()),
                () -> assertThat(stationResponse3.getName()).isEqualTo(station3.getName())
        );
    }

    private Long getCreatedLineId(Long upStationId, Long downStationId) {
        LineRequest lineRequest = new LineRequest(
                "2호선", "green", upStationId, downStationId, 10, 200);
        return Long.parseLong(createLine(lineRequest).header("Location").split("/")[2]);
    }
}
