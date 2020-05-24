package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.admin.acceptance.AcceptanceTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.StationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class LineStationManageAcceptanceTest {
    private final StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();
    private final LineStationAcceptanceTest lineStationAcceptanceTest = new LineStationAcceptanceTest();
    private final LineAcceptanceTest lineAcceptanceTest = new LineAcceptanceTest();

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        stationAcceptanceTest.createStation(STATION_NAME_KANGNAM);
        stationAcceptanceTest.createStation(STATION_NAME_YEOKSAM);
        stationAcceptanceTest.createStation(STATION_NAME_SEOLLEUNG);

        lineAcceptanceTest.createLine("2호선");

        StationResponse kangNamStation = stationAcceptanceTest.getStation(1L);
        StationResponse yeokSamStation = stationAcceptanceTest.getStation(2L);
        StationResponse seolLeungStation = stationAcceptanceTest.getStation(3L);

        LineDetailResponse line2 = lineAcceptanceTest.getLine(1L);

        lineStationAcceptanceTest.addLineStation(line2.getId(), null, kangNamStation.getId());
        lineStationAcceptanceTest.addLineStation(line2.getId(), kangNamStation.getId(), yeokSamStation.getId());
        lineStationAcceptanceTest.addLineStation(line2.getId(), yeokSamStation.getId(), seolLeungStation.getId());

        line2 = lineAcceptanceTest.getLine(1L);

        assertThat(line2.getStations()).hasSize(3);

        lineStationAcceptanceTest.deleteLineStation(line2.getId(), yeokSamStation.getId());

        line2 = lineAcceptanceTest.getLine(line2.getId());
        assertThat(line2.getStations().size()).isEqualTo(2);
    }
}
