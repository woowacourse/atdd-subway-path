package wooteco.subway.acceptance;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.path.PathFindRequest;
import wooteco.subway.dto.section.SectionSaveRequest;
import wooteco.subway.dto.station.StationSaveRequest;

public class PathFindAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("경로를 조회한다.")
    void findPath() {
        // given
        Long stationId1 = postStations(new StationSaveRequest("강남역"));
        Long stationId2 = postStations(new StationSaveRequest("역삼역"));
        Long stationId3 = postStations(new StationSaveRequest("선릉역"));
        Long lineId = postLines(new LineSaveRequest("신분당선", "bg-red-600", stationId1, stationId3, 9, 900));
        postSections(lineId, new SectionSaveRequest(stationId2, stationId3, 6));

        // when
        PathFindRequest pathFindRequest = new PathFindRequest(stationId1, stationId3, 15);
        given().log().all()
                .param("source", stationId1)
                .param("target", stationId3)
                .param("age", 15)
                .when()
                .get("/paths")
                // then
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
