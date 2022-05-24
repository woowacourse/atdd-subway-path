package wooteco.subway.acceptance;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import wooteco.subway.dao.LineDaoImpl;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.SectionRequest;
import wooteco.subway.ui.dto.StationRequest;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDaoImpl lineDaoImpl;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        clearAllStations();
        clearAllLines();
    }

    public void clearAllStations() {
        List<Station> stations = stationDao.findAll();
        List<Long> stationIds = stations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        for (Long stationId : stationIds) {
            stationDao.deleteById(stationId);
        }
    }

    public void clearAllLines() {
        List<Line> lines = lineDaoImpl.findAll();
        List<Long> lineIds = lines.stream()
                .map(Line::getId)
                .collect(Collectors.toList());

        for (Long lineId : lineIds) {
            lineDaoImpl.deleteById(lineId);
        }
    }

    public ValidatableResponse getLineRequest(String uri) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all();
    }

    public ValidatableResponse createLineRequest(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when()
                .post("/lines")
                .then().log().all();
    }

    public ValidatableResponse createStationRequest(StationRequest stationRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when()
                .post("/stations")
                .then().log().all();
    }

    public ValidatableResponse createSectionRequest(SectionRequest sectionRequest, String uri) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri + "/sections")
                .then().log().all();
    }

    public long getSavedStationIdByResponse(ExtractableResponse<Response> response1) {
        return Long.parseLong(response1.header("Location").split("/")[2]);
    }
}
