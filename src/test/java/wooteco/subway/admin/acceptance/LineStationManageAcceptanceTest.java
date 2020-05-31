package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
public class LineStationManageAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);

        createLine("2호선");

        StationResponse kangNamStation = getStation(1L);
        StationResponse yeokSamStation = getStation(2L);
        StationResponse seolLeungStation = getStation(3L);

        LineDetailResponse line2 = getLine(1L);

        addLineStation(line2.getId(), null, kangNamStation.getId());
        addLineStation(line2.getId(), kangNamStation.getId(), yeokSamStation.getId());
        addLineStation(line2.getId(), yeokSamStation.getId(), seolLeungStation.getId());

        line2 = getLine(1L);

        assertThat(line2.getStations()).hasSize(3);

        deleteLineStation(line2.getId(), yeokSamStation.getId());

        line2 = getLine(line2.getId());
        assertThat(line2.getStations().size()).isEqualTo(2);
    }

    public void createLine(String name) {
        String path = "/api/lines";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");
        params.put("backgroundColor", "bg-gray-300");

        super.post(params, path);
    }

    public LineDetailResponse getLine(Long id) {
        String path = "/api/lines/" + id;
        return super.get(path, LineDetailResponse.class);
    }

    public void createStation(String name) {
        String path = "/api/stations";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        super.post(params, path);
    }

    public StationResponse getStation(Long id) {
        String path = "/api/stations/" + id;
        return super.get(path, StationResponse.class);
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    protected void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId == null ? "" : preStationId.toString());
        params.put("stationId", stationId.toString());
        params.put("distance", distance.toString());
        params.put("duration", duration.toString());
        String path = "/api/lines/" + lineId + "/stations";

        super.post(params, path);
    }

    public void deleteLineStation(Long lineId, Long stationId) {
        String path = "api/lines/" + lineId + "/stations/" + stationId;

        super.delete(path);
    }
}
