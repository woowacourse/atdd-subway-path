package wooteco.subway.acceptance;

import static wooteco.subway.acceptance.LineAcceptanceTest.postLines;
import static wooteco.subway.acceptance.StationAcceptanceTest.postStations;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.section.SectionSaveRequest;
import wooteco.subway.dto.station.StationSaveRequest;

public class SectionAcceptanceTest extends AcceptanceTest {

    public static void postSections(final Long lineId, final SectionSaveRequest sectionSaveRequest) {
        RestAssured.given().log().all()
                .body(sectionSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all();
    }

    @Test
    @DisplayName("구간을 추가한다.")
    void saveSection() {
        // given
        Long stationId1 = postStations(new StationSaveRequest("강남역"));
        Long stationId2 = postStations(new StationSaveRequest("역삼역"));
        Long stationId3 = postStations(new StationSaveRequest("선릉역"));
        Long lineId = postLines(new LineSaveRequest("신분당선", "bg-red-600", stationId1, stationId3, 10, 900));
        SectionSaveRequest sectionSaveRequest = new SectionSaveRequest(stationId1, stationId2, 3);

        // when
        RestAssured.given().log().all()
                .body(sectionSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                // then
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void removeSection() {
        // given
        Long stationId1 = postStations(new StationSaveRequest("강남역"));
        Long stationId2 = postStations(new StationSaveRequest("역삼역"));
        Long stationId3 = postStations(new StationSaveRequest("선릉역"));
        Long lineId = postLines(new LineSaveRequest("신분당선", "bg-red-600", stationId1, stationId2, 10, 900));
        postSections(lineId, new SectionSaveRequest(stationId2, stationId3, 10));

        // when
        RestAssured.given().log().all()
                .param("stationId", stationId2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/lines/" + lineId + "/sections")
                // then
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
