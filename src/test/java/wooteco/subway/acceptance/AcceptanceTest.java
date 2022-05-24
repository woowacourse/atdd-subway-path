package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import wooteco.subway.dto.line.LineResponse;
import wooteco.subway.dto.line.LineSaveRequest;
import wooteco.subway.dto.section.SectionSaveRequest;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.dto.station.StationSaveRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema-truncate.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

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

    public static Long postLines(final LineSaveRequest lineSaveRequest) {
        return RestAssured.given().log().all()
                .body(lineSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract()
                .response()
                .as(LineResponse.class)
                .getId();
    }

    public static void postSections(final Long lineId, final SectionSaveRequest sectionSaveRequest) {
        RestAssured.given().log().all()
                .body(sectionSaveRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all();
    }
}
