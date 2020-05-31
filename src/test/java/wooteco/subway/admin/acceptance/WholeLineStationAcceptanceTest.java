package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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
public class WholeLineStationAcceptanceTest extends AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("전체 노선별 지하철 역 전체 조회")
    @Test
    void showWholeLineStation() {
        createLine("신분당선");
        createLine("2호선");

        LineDetailResponse shinBunDangLine = getLine(1L);
        LineDetailResponse line2 = getLine(2L);

        createStation("강남역");
        createStation("역삼역");
        createStation("선릉역");
        createStation("양재역");
        createStation("양재시민숲역");

        StationResponse gangNamStation = getStation(1L);
        StationResponse yeokSamStation = getStation(2L);
        StationResponse seolLeungStation = getStation(3L);
        StationResponse yangJaeStation = getStation(4L);
        StationResponse yangJaeCitizensForestStation = getStation(5L);

        addLineStation(shinBunDangLine.getId(), null, gangNamStation.getId());
        addLineStation(shinBunDangLine.getId(), gangNamStation.getId(),
            yeokSamStation.getId());
        addLineStation(shinBunDangLine.getId(), yeokSamStation.getId(),
            seolLeungStation.getId());

        addLineStation(line2.getId(), null, gangNamStation.getId());
        addLineStation(line2.getId(), gangNamStation.getId(), yangJaeStation.getId());
        addLineStation(line2.getId(), yangJaeStation.getId(),
            yangJaeCitizensForestStation.getId());

        List<LineDetailResponse> lineDetailResponses = getLineDetails();

        assertThat(lineDetailResponses.size()).isEqualTo(2);
        assertThat(lineDetailResponses.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetailResponses.get(1).getStations().size()).isEqualTo(3);
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

    public List<LineDetailResponse> getLineDetails() {
        String path = "/api/lines/detail";

        return super.getList(path, LineDetailResponse.class);
    }
}
