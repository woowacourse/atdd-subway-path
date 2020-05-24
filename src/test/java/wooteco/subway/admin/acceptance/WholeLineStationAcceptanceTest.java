package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.admin.acceptance.util.LineAcceptanceTest;
import wooteco.subway.admin.acceptance.util.LineStationAcceptanceTest;
import wooteco.subway.admin.acceptance.util.StationAcceptanceTest;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class WholeLineStationAcceptanceTest {
    private final LineAcceptanceTest lineAcceptanceTest = new LineAcceptanceTest();
    private final StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();
    private final LineStationAcceptanceTest lineStationAcceptanceTest = new LineStationAcceptanceTest();

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("전체 노선별 지하철 역 전체 조회")
    @Test
    void showWholeLineStation() {
        lineAcceptanceTest.createLine("신분당선");
        lineAcceptanceTest.createLine("2호선");

        LineDetailResponse shinBunDangLine = lineAcceptanceTest.getLine(1L);
        LineDetailResponse line2 = lineAcceptanceTest.getLine(2L);

        stationAcceptanceTest.createStation("강남역");
        stationAcceptanceTest.createStation("역삼역");
        stationAcceptanceTest.createStation("선릉역");
        stationAcceptanceTest.createStation("양재역");
        stationAcceptanceTest.createStation("양재시민숲역");

        StationResponse gangNamStation = stationAcceptanceTest.getStation(1L);
        StationResponse yeokSamStation = stationAcceptanceTest.getStation(2L);
        StationResponse seolLeungStation = stationAcceptanceTest.getStation(3L);
        StationResponse yangJaeStation = stationAcceptanceTest.getStation(4L);
        StationResponse yangJaeCitizensForestStation = stationAcceptanceTest.getStation(5L);

        lineStationAcceptanceTest.addLineStation(shinBunDangLine.getId(), null, gangNamStation.getId());
        lineStationAcceptanceTest.addLineStation(shinBunDangLine.getId(), gangNamStation.getId(), yeokSamStation.getId());
        lineStationAcceptanceTest.addLineStation(shinBunDangLine.getId(), yeokSamStation.getId(), seolLeungStation.getId());

        lineStationAcceptanceTest.addLineStation(line2.getId(), null, gangNamStation.getId());
        lineStationAcceptanceTest.addLineStation(line2.getId(), gangNamStation.getId(), yangJaeStation.getId());
        lineStationAcceptanceTest.addLineStation(line2.getId(), yangJaeStation.getId(), yangJaeCitizensForestStation.getId());

        List<LineDetailResponse> lineDetailResponses = lineAcceptanceTest.getLineDetails();

        assertThat(lineDetailResponses.size()).isEqualTo(2);
        assertThat(lineDetailResponses.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetailResponses.get(1).getStations().size()).isEqualTo(3);
    }
}
