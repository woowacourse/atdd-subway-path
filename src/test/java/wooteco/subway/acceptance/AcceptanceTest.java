package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    protected Long createStation(final String name) {
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/stations",
                new StationRequest(name));

        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    protected LineResponse createLine(String name, String color, Long upStationId, Long downStationId, int distance,
                                      int extraFare) {
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/lines",
                new LineRequest(name, color, upStationId, downStationId, distance, extraFare));
        return response.jsonPath().getObject(".", LineResponse.class);
    }

    protected void addSection(Long upStationId, Long downStationId, int distance, Long lineId) {
        AcceptanceTestFixture.post("/lines/" + lineId + "/sections",
                new SectionRequest(upStationId, downStationId, distance));
    }
}
