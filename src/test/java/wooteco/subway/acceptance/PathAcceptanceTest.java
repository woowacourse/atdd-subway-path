package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.station.StationRequest;

@DisplayName("지하철 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    private static final StationRequest 강남역 = new StationRequest("강남역");

    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long stationId4;
    private Long stationId5;
    private Long lineId1;
    private Long lineId2;

    @BeforeEach
    void beforeEach() {
        stationId1 = postStationId(강남역);
        stationId2 = postStationId(대흥역);
        stationId3 = postStationId(공덕역);
        stationId4 = postStationId(광흥창역);
        stationId5 = postStationId(상수역);

        LineRequest 분당선 = new LineRequest("분당선", "bg-green-600", stationId1, stationId2, 2, 100);
        LineRequest 다른분당선 = new LineRequest("다른분당선", "bg-red-600", stationId2, stationId5, 3, 200);
        lineId1 = postLineId(분당선);
        lineId2 = postLineId(다른분당선);
    }

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void findPath() {
        // given
        SectionRequest section1 = new SectionRequest(stationId2, stationId3, 2);
        SectionRequest section2 = new SectionRequest(stationId3, stationId4, 7);
        SectionRequest section3 = new SectionRequest(stationId5, stationId4, 4);

        postSectionResponse(lineId1, section1);
        postSectionResponse(lineId1, section2);
        postSectionResponse(lineId2, section3);

        // when
        ValidatableResponse validatableResponse = RestAssured.given()
                .log().all()
                .when()
                .get("/paths?source=" + stationId1 + "&target=" + stationId4 + "&age=" + 10)
                .then().log().all();

        // then
        assertThat(validatableResponse.extract().statusCode()).isEqualTo(HttpStatus.OK.value());
        validatableResponse
                .body("stations.id", contains(stationId1.intValue(),
                        stationId2.intValue(),
                        stationId5.intValue(),
                        stationId4.intValue()))
                .body("distance", equalTo(9))
                .body("fare", equalTo(1250));
    }

    @DisplayName("존재하지 않는 지하철 경로를 조회한다.")
    @Test
    void findNotExistPath() {
        // given
        SectionRequest section1 = new SectionRequest(stationId2, stationId3, 2);
        SectionRequest section2 = new SectionRequest(stationId4, stationId5, 4);

        postSectionResponse(lineId1, section1);
        postSectionResponse(lineId2, section2);

        // when
        ValidatableResponse validatableResponse = RestAssured.given()
                .log().all()
                .when()
                .get("/paths?source=" + stationId1 + "&target=" + stationId4 + "&age=" + 10)
                .then().log().all();

        // then
        assertThat(validatableResponse.extract().statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
