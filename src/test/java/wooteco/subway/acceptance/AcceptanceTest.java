package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.dto.line.LineResponse;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceTest {

    protected static final String NOT_FOUND_ERROR_MESSAGE = "존재하지 않습니다";
    protected static final String BLANK_OR_NULL_ERROR_MESSAGE = "빈 값";

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> createStationAndReturnResponse(final String stationName) {
        return RestAssured.given()
                .body(new StationRequest(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().extract();
    }

    protected StationResponse createStation(final String stationName) {
        return RestAssured.given()
                .body(new StationRequest(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().extract()
                .as(StationResponse.class);
    }

    protected LineResponse createLine(
            final String name, final String color, final long upStationId, final long downStationId, final int distance
    ) {
        LineSaveRequest lineRequest = new LineSaveRequest(name, color, upStationId, downStationId, distance);
        return RestAssured
                .given()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().extract()
                .as(LineResponse.class);
    }

    protected ExtractableResponse<Response> createLineAndReturnResponse(
            final String name, final String color, final long upStationId, final long downStationId, final int distance
    ) {
        LineSaveRequest lineRequest = new LineSaveRequest(name, color, upStationId, downStationId, distance);
        return RestAssured
                .given()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .extract();
    }

    protected ExtractableResponse<Response> createLineAndReturnResponse(
            final String name, final String color, final long upStationId, final long downStationId, final int distance, final int extraFare
    ) {
        LineSaveRequest lineRequest = new LineSaveRequest(name, color, upStationId, downStationId, distance, extraFare);
        return RestAssured
                .given()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .extract();
    }

    protected void addSection(final LineResponse createdLine, final StationResponse upStationResponse,
                              final StationResponse downStationResponse, final int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStationResponse.getId(), downStationResponse.getId(),
                distance);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when()
                .post("/lines/" + createdLine.getId() + "/sections")
                .then()
                .extract();
    }

    protected LineResponse createLine(final String name, final String color, final Long upStationId,
                                    final Long downStationId, final int distance, final int extraFare) {
        LineSaveRequest lineRequest = new LineSaveRequest(name, color, upStationId, downStationId, distance, extraFare);
        return RestAssured
                .given()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().extract()
                .as(LineResponse.class);
    }

    protected ExtractableResponse<Response> createLineAndReturnResponse(final String name, final String color,
                                                                        final Long upStationId,
                                                                        final Long downStationId,
                                                                        final int distance,
                                                                        final int extraFare) {

        LineSaveRequest lineRequest = new LineSaveRequest(name, color, upStationId, downStationId, distance, extraFare);
        return RestAssured
                .given()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .extract();
    }
}
