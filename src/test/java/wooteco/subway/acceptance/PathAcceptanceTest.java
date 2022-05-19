package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.station.StationRequest;

class PathAcceptanceTest extends AcceptanceTest {

    private static final String SOURCE_STATION_ID = "source";
    private static final String TARGET_STATION_ID = "target";

    private static final String STATION_NAMES = "stations.name";
    private static final String DISTANCE = "distance";
    private static final String FARE = "fare";

    private Station gangnam;
    private Station yeoksam;
    private Station seolleung;
    private Station samsung;

    private Station seoulForest;
    private Station wangsimni;

    private Station heangdang;
    private Station majang;
    private Station dapsimni;

    private Station yacksu;
    private Station geumho;
    private Station oksu;

    @BeforeEach
    void setUpData() {
        gangnam = createStation(new StationRequest(GANGNAM)).as(Station.class);
        yeoksam = createStation(new StationRequest(YEOKSAM)).as(Station.class);
        seolleung = createStation(new StationRequest(SEOLLEUNG)).as(Station.class);
        samsung = createStation(new StationRequest(SAMSUNG)).as(Station.class);

        seoulForest = createStation(new StationRequest(SEOUL_FOREST)).as(Station.class);
        wangsimni = createStation(new StationRequest(WANGSIMNI)).as(Station.class);

        heangdang = createStation(new StationRequest(HEANGDANG)).as(Station.class);
        majang = createStation(new StationRequest(MAJANG)).as(Station.class);
        dapsimni = createStation(new StationRequest(DAPSIMNI)).as(Station.class);

        yacksu = createStation(new StationRequest(YACKSU)).as(Station.class);
        geumho = createStation(new StationRequest(GEUMHO)).as(Station.class);
        oksu = createStation(new StationRequest(OKSU)).as(Station.class);

        final long greenLineId = createAndGetLineId(
                new LineRequest("2호선", "green", gangnam.getId(), yeoksam.getId(), 10));
        createSection(new SectionRequest(yeoksam.getId(), seolleung.getId(), 8), greenLineId);
        createSection(new SectionRequest(seolleung.getId(), samsung.getId(), 5), greenLineId);

        final long yellowLineId = createAndGetLineId(
                new LineRequest("수인분당선", "yellow", seolleung.getId(), seoulForest.getId(), 12));
        createSection(new SectionRequest(seoulForest.getId(), wangsimni.getId(), 7), yellowLineId);

        final long purpleLineId = createAndGetLineId(
                new LineRequest("5호선", "purple", heangdang.getId(), wangsimni.getId(), 11));
        createSection(new SectionRequest(wangsimni.getId(), majang.getId(), 17), purpleLineId);
        createSection(new SectionRequest(majang.getId(), dapsimni.getId(), 15), purpleLineId);

        final long orangeLineId = createAndGetLineId(
                new LineRequest("3호선", "orange", yacksu.getId(), geumho.getId(), 7));
        createSection(new SectionRequest(geumho.getId(), oksu.getId(), 12), orangeLineId);
    }

    @Test
    @DisplayName("동일한 역의 경로를 조회할 경우 400 을 응답한다.")
    void ShowPath_SameStations_BadRequestReturned() {
        // when
        final ValidatableResponse response = getResponse(gangnam.getId(), gangnam.getId());

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ValidatableResponse getResponse(final Long sourceStationId, final Long targetStationId) {
        return RestAssured.given().log().all()
                .queryParam(SOURCE_STATION_ID, sourceStationId)
                .queryParam(TARGET_STATION_ID, targetStationId)
                .when()
                .get(PATH_URL_PREFIX)
                .then().log().all();
    }

    @Test
    @DisplayName("이동할 수 없는 경로를 조회할 경우 404 을 응답한다.")
    void ShowPath_InvalidPath_BadRequestReturned() {
        // when
        final ValidatableResponse response = getResponse(gangnam.getId(), oksu.getId());

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("환승을 한 번하는 출발역과 도착역의 경로 정보를 조회한다.")
    void ShowPath_TransferOnce_OK() {
        // given
        final String[] expectedStationNames = {
                GANGNAM,
                YEOKSAM,
                SEOLLEUNG,
                SEOUL_FOREST
        };
        final int expectedDistance = 30;
        final int expectedFare = 1650;

        // when
        final ValidatableResponse response = getResponse(gangnam.getId(), seoulForest.getId());

        // then
        response.statusCode(HttpStatus.OK.value())
                .body(STATION_NAMES, hasItems(expectedStationNames))
                .body(DISTANCE, equalTo(expectedDistance))
                .body(FARE, equalTo(expectedFare));
    }

    @Test
    @DisplayName("환승을 두 번하는 출발역과 도착역의 경로 정보를 조회한다.")
    void ShowPath_TransferTwice_OK() {
        // given
        final String[] expectedStationNames = {
                YEOKSAM,
                SEOLLEUNG,
                SEOUL_FOREST,
                WANGSIMNI,
                MAJANG,
                DAPSIMNI
        };
        final int expectedDistance = 59;
        final int expectedFare = 2250;

        // when
        final ValidatableResponse response = getResponse(yeoksam.getId(), dapsimni.getId());

        // then
        response.statusCode(HttpStatus.OK.value())
                .body(STATION_NAMES, hasItems(expectedStationNames))
                .body(DISTANCE, equalTo(expectedDistance))
                .body(FARE, equalTo(expectedFare));
    }

    @Test
    @DisplayName("경로 조회시 파라미터 데이터가 정확하지 않은 경우 400 을 응답한다.")
    void ShowPath_InvalidParameter_BadRequestReturned() {
        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .queryParam("source", gangnam.getId())
                .when()
                .get(PATH_URL_PREFIX)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("요청 값 형식이 올바르지 않습니다."));
    }
}
