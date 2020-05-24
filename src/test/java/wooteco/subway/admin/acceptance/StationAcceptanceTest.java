package wooteco.subway.admin.acceptance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wooteco.subway.admin.dto.StationResponse;

public class StationAcceptanceTest extends AcceptanceTest {

    void createStation(String name) {
        String path = "/api/stations";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        super.post(params, path);
    }

    StationResponse getStation(Long id) {
        String path = "/api/stations/" + id;
        return super.get(path, StationResponse.class);
    }

    void deleteStation(Long id) {
        String path = "/api/stations/" + id;

        super.delete(path);
    }

    List<StationResponse> getStations() {
        String path = "/api/stations";

        return super.getList(path, StationResponse.class);
    }
}
