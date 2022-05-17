package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.LineAcceptanceTest.postLines;
import static wooteco.subway.acceptance.SectionAcceptanceTest.postSections;
import static wooteco.subway.acceptance.StationAcceptanceTest.postStations;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.line.LineResponse;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.section.SectionSaveRequest;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.dto.station.StationSaveRequest;

public class PathFindAcceptanceTest extends AcceptanceTest {

    private ExtractableResponse<Response> getPath(final Long stationId1, final Long stationId3) {
        return RestAssured.given().log().all()
                .param("source", stationId1)
                .param("target", stationId3)
                .param("age", 15)
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("경로를 조회한다.")
    void findPath() {
        // given
        Long stationId1 = postStations(new StationSaveRequest("강남역"))
                .as(StationResponse.class)
                .getId();
        Long stationId2 = postStations(new StationSaveRequest("역삼역"))
                .as(StationResponse.class)
                .getId();
        Long stationId3 = postStations(new StationSaveRequest("선릉역"))
                .as(StationResponse.class)
                .getId();
        Long lineId = postLines(new LineSaveRequest("신분당선", "bg-red-600", stationId1, stationId3, 10))
                .as(LineResponse.class)
                .getId();
        postSections(lineId, new SectionSaveRequest(stationId2, stationId3, 6));

        // when
        ExtractableResponse<Response> response = getPath(stationId1, stationId3);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


}
