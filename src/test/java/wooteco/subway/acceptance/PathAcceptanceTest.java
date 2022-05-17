package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.station.StationRequest;

class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("동일한 역의 경로를 조회할 경우 400 을 응답한다.")
    void ShowPath_SameStations_BadRequestReturned() {
        // given
        final long gangnamId = createAndGetStationId(new StationRequest("강남역"));
        final long yeoksamId = createAndGetStationId(new StationRequest("역삼역"));
        final long seolleungId = createAndGetStationId(new StationRequest("선릉역"));

        final long lineId = createAndGetLineId(new LineRequest("2호선", "green", gangnamId, yeoksamId, 10));

        createSection(new SectionRequest(yeoksamId, seolleungId, 8), lineId);

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParam("source", gangnamId)
                .queryParam("target", gangnamId)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
