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

        final long orangeLineId = createAndGetLineId(
                new LineRequest("3호선", "orange", yacksu.getId(), geumho.getId(), 7));
        createSection(new SectionRequest(geumho.getId(), oksu.getId(), 12), orangeLineId);
    }

    @Test
    @DisplayName("동일한 역의 경로를 조회할 경우 400 을 응답한다.")
    void ShowPath_SameStations_BadRequestReturned() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParam("source", gangnam.getId())
                .queryParam("target", gangnam.getId())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("이동할 수 없는 경로를 조회할 경우 404 을 응답한다.")
    void ShowPath_InvalidPath_BadRequestReturned() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParam("source", gangnam.getId())
                .queryParam("target", oksu.getId())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("출발역과 도착역의 경로 정보를 조회한다.")
    void ShowPath() {
        // given
        final List<Station> expectedStations = List.of(
                gangnam,
                yeoksam,
                seolleung,
                seoulForest
        );
        final Distance expectedDistance = new Distance(30);
        final PathResponse expected = PathResponse.of(expectedStations, expectedDistance, new Fare(expectedDistance));

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParam("source", gangnam.getId())
                .queryParam("target", seoulForest.getId())
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        final PathResponse actual = response.as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }
}
