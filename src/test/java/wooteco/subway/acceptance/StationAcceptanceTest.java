package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.station.StationRequest;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private final StationRequest gangNamStationRequest = new StationRequest(GANGNAM);
    private final StationRequest yeokSamStationRequest = new StationRequest(YEOKSAM);

    @DisplayName("지하철역을 생성한다.")
    @Test
    void CreateStation() {
        // when
        final ValidatableResponse response = requestPost(gangNamStationRequest, STATION_URL_PREFIX);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(LOCATION, notNullValue());
    }

    @DisplayName("생성하려는 역의 이름이 중복되면 BadRequest 를 반환한다.")
    @Test
    void CreateStation_DuplicateName_BadRequest() {
        // given
        requestPost(gangNamStationRequest, STATION_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPost(gangNamStationRequest, STATION_URL_PREFIX);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void ShowStations() {
        /// given
        requestPost(gangNamStationRequest, STATION_URL_PREFIX);
        requestPost(yeokSamStationRequest, STATION_URL_PREFIX);

        // when
        final ValidatableResponse response = requestGet(STATION_URL_PREFIX);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("name", contains(GANGNAM, YEOKSAM));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void DeleteStation() {
        // given
        final long id = createAndGetId(gangNamStationRequest, STATION_URL_PREFIX);

        // when
        final ValidatableResponse response = requestDelete(STATION_URL_PREFIX + "/" + id);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 역을 제거하면 404를 반환한다.")
    @Test
    void DeleteStation_NotExistId_BadRequest() {
        // when
        final ValidatableResponse response = requestDelete(STATION_URL_PREFIX + "/999");

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
