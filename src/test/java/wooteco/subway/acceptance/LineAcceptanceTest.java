package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.line.LineUpdateRequest;
import wooteco.subway.dto.station.StationSaveRequest;

class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("노선을 추가한다.")
    void save() {
        //given
        Long upStationId = postStations(new StationSaveRequest("강남역"));
        Long downStationId = postStations(new StationSaveRequest("역삼역"));
        LineSaveRequest lineSaveRequest = new LineSaveRequest("신분당선", "bg-red-600", upStationId, downStationId, 3, 900);
        RestAssured.given().log().all()
                .body(lineSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // when
                .when()
                .post("/lines")
                // then
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @Test
    @DisplayName("기존에 존재하는 노선의 이름으로 노선을 생성하면 노선을 생성할 수 없다.")
    void createLinesWithExistNames() {
        // given
        Long upStationId = postStations(new StationSaveRequest("강남역"));
        Long downStationId = postStations(new StationSaveRequest("역삼역"));
        LineSaveRequest lineSaveRequest = new LineSaveRequest("신분당선", "bg-red-600", upStationId, downStationId, 3, 900);
        postLines(lineSaveRequest);

        // when
        RestAssured.given().log().all()
                .body(lineSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                // then
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("전체 노선 목록을 조회한다.")
    void findAllLines() {
        //given
        Long upStationId = postStations(new StationSaveRequest("강남역"));
        Long downStationId = postStations(new StationSaveRequest("역삼역"));
        postLines(new LineSaveRequest("신분당선", "bg-red-600", upStationId, downStationId, 3, 900));
        postLines(new LineSaveRequest("분당선", "bg-green-600", upStationId, downStationId, 5, 0));

        // when
        RestAssured.given().log().all()
                .when()
                .get("/lines")
                // then
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.hasSize(2));
    }

    @Test
    @DisplayName("단일 노선을 조회한다.")
    void findLine() {
        // given
        Long station1Id = postStations(new StationSaveRequest("강남역"));
        Long station2Id = postStations(new StationSaveRequest("역삼역"));
        Long lineId = postLines(new LineSaveRequest("신분당선", "bg-red-600", station1Id, station2Id, 3, 900));

        // when
        RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                // then
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선을 수정한다.")
    void updateLine() {
        //given
        Long upStationId = postStations(new StationSaveRequest("강남역"));
        Long downStationId = postStations(new StationSaveRequest("역삼역"));
        Long lineId = postLines(new LineSaveRequest("신분당선", "bg-red-600", upStationId, downStationId, 3, 900));

        // when
        RestAssured.given().log().all()
                .body(new LineUpdateRequest("분당선", "bg-green-600", 600))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + lineId)
                // then
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void deleteLine() {
        //given
        Long upStationId = postStations(new StationSaveRequest("강남역"));
        Long downStationId = postStations(new StationSaveRequest("역삼역"));
        Long lineId = postLines(new LineSaveRequest("신분당선", "bg-red-600", upStationId, downStationId, 3, 900));

        // when
        RestAssured.given().log().all()
                .when()
                .delete("/lines/" + lineId)
                // then
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
