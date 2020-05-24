package wooteco.subway.admin.acceptance;

import java.util.HashMap;
import java.util.Map;

public class LineStationAcceptanceTest extends AcceptanceTest {

    void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
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
