package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class PathManageAcceptanceTest {
    private final StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();
    private final LineAcceptanceTest lineAcceptanceTest = new LineAcceptanceTest();
    private final LineStationAcceptanceTest lineStationAcceptanceTest = new LineStationAcceptanceTest();
    private final PathAcceptanceTest pathAcceptanceTest = new PathAcceptanceTest();

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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

        lineAcceptanceTest.createLine("신분당선");
        lineAcceptanceTest.createLine("2호선");

        LineDetailResponse shinBunDangLine = lineAcceptanceTest.getLine(1L);
        LineDetailResponse line2 = lineAcceptanceTest.getLine(2L);

        lineStationAcceptanceTest.addLineStation(shinBunDangLine.getId(), null, gangNamStation.getId());
        lineStationAcceptanceTest.addLineStation(shinBunDangLine.getId(), gangNamStation.getId(),
            yangJaeStation.getId());
        lineStationAcceptanceTest.addLineStation(shinBunDangLine.getId(), yangJaeStation.getId(),
            yangJaeCitizensForestStation.getId());

        lineStationAcceptanceTest.addLineStation(line2.getId(), null, gangNamStation.getId());
        lineStationAcceptanceTest.addLineStation(line2.getId(), gangNamStation.getId(), yeokSamStation.getId());
        lineStationAcceptanceTest.addLineStation(line2.getId(), yeokSamStation.getId(), seolLeungStation.getId());
    }

    @DisplayName("최단 거리 찾기")
    @Test
    void findShortestDistance() {
        PathResponse pathResponse = pathAcceptanceTest.findShortestPath(5L, 3L, "DISTANCE");

        assertThat(pathResponse.getStations().size()).isEqualTo(5);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
    }

    @DisplayName("최단 시간 찾기")
    @Test
    void findShortestDuration() {
        PathResponse pathResponse = pathAcceptanceTest.findShortestPath(5L, 3L, "DURATION");

        assertThat(pathResponse.getStations().size()).isEqualTo(5);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(40);
    }
}
