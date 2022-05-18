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
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.station.StationRequest;

class PathAcceptanceTest extends AcceptanceTest {

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
        gangnam = createStation(new StationRequest("강남역")).as(Station.class);
        yeoksam = createStation(new StationRequest("역삼역")).as(Station.class);
        seolleung = createStation(new StationRequest("선릉역")).as(Station.class);
        samsung = createStation(new StationRequest("삼성역")).as(Station.class);

        seoulForest = createStation(new StationRequest("서울숲역")).as(Station.class);
        wangsimni = createStation(new StationRequest("왕십리역")).as(Station.class);

        heangdang = createStation(new StationRequest("행당역")).as(Station.class);
        majang = createStation(new StationRequest("마장역")).as(Station.class);
        dapsimni = createStation(new StationRequest("답십리역")).as(Station.class);

        yacksu = createStation(new StationRequest("약수역")).as(Station.class);
        geumho = createStation(new StationRequest("금호역")).as(Station.class);
        oksu = createStation(new StationRequest("옥수역")).as(Station.class);

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
        final ExtractableResponse<Response> response = getResponseExtractableResponse(gangnam.getId(), gangnam.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> getResponseExtractableResponse(final Long sourceStationId,
                                                                         final Long targetStationId) {
        return RestAssured.given().log().all()
                .queryParam("source", sourceStationId)
                .queryParam("target", targetStationId)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("이동할 수 없는 경로를 조회할 경우 404 을 응답한다.")
    void ShowPath_InvalidPath_BadRequestReturned() {
        // when
        final ExtractableResponse<Response> response = getResponseExtractableResponse(gangnam.getId(), oksu.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("환승을 한 번하는 출발역과 도착역의 경로 정보를 조회한다.")
    void ShowPath_TransferOnce_OK() {
        // given
        final List<Station> expectedStations = List.of(
                gangnam,
                yeoksam,
                seolleung,
                seoulForest
        );
        final Distance expectedDistance = new Distance(30);
        final PathResponse expected = PathResponse.of(expectedStations, expectedDistance, new Fare(1650));

        // when
        final ExtractableResponse<Response> response = getResponseExtractableResponse(gangnam.getId(),
                seoulForest.getId());

        final PathResponse actual = response.as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("환승을 두 번하는 출발역과 도착역의 경로 정보를 조회한다.")
    void ShowPath_TransferTwice_OK() {
        // given
        final List<Station> expectedStations = List.of(
                yeoksam,
                seolleung,
                seoulForest,
                wangsimni,
                majang,
                dapsimni
        );
        final Distance expectedDistance = new Distance(59);
        final PathResponse expected = PathResponse.of(expectedStations, expectedDistance, new Fare(2250));

        // when
        final ExtractableResponse<Response> response = getResponseExtractableResponse(yeoksam.getId(),
                dapsimni.getId());

        final PathResponse actual = response.as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("경로 조회시 파라미터 데이터가 정확하지 않은 경우 400 을 응답한다.")
    void ShowPath_InvalidParameter_BadRequestReturned() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParam("source", gangnam.getId())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
