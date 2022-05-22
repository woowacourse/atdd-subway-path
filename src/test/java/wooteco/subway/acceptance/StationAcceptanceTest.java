package wooteco.subway.acceptance;


import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.dto.station.StationSaveRequest;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    public static Long postStations(final StationSaveRequest stationSaveRequest) {
        return RestAssured.given().log().all()
                .body(stationSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract()
                .response()
                .as(StationResponse.class)
                .getId();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        StationSaveRequest stationSaveRequest = new StationSaveRequest("강남역");
        RestAssured.given().log().all()
                .body(stationSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                // when
                .when()
                .post("/stations")
                // then
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", notNullValue());
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성할 수 없다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        StationSaveRequest stationSaveRequest = new StationSaveRequest("강남역");
        postStations(stationSaveRequest);

        // when
        RestAssured.given().log().all()
                .body(stationSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                // then
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        postStations(new StationSaveRequest("강남역"));
        postStations(new StationSaveRequest("역삼역"));

        // when
        RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                // then
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.hasSize(2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Long stationId = postStations(new StationSaveRequest("강남역"));
        RestAssured.given().log().all()
                // when
                .when()
                .delete("/stations/" + stationId)
                // then
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
