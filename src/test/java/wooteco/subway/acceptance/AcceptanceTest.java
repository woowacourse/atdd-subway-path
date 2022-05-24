package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.ui.dto.SectionRequest;
import wooteco.subway.ui.dto.StationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest extends DBTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    public void reset() {
        jdbcTemplate.execute("DELETE FROM section");
        jdbcTemplate.execute("DELETE FROM station");
        jdbcTemplate.execute("DELETE FROM line");
        jdbcTemplate.execute("ALTER TABLE station ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE line ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE section ALTER COLUMN id RESTART WITH 1");
    }

    ExtractableResponse<Response> createStation(StationRequest stationRequest) {
        return RestAssured.given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> createLine(String name, String color, Long upStationId,
                                             Long downStationId, Integer distance, Integer extraFare) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("extraFare", extraFare);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> createSection(long lineId, SectionRequest sectionRequest) {
        return RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> findLineById(Long lineId) {
        return RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .extract();
    }
}
