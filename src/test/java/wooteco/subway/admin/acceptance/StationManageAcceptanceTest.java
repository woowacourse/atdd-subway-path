package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.StationResponse;

public class StationManageAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 관리한다")
    @Test
    void manageStation() {
        // when
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        // then
        List<StationResponse> stations = getStations();
        assertThat(stations.size()).isEqualTo(3);

        // when
        deleteStation(stations.get(0).getId());
        // then
        List<StationResponse> stationsAfterDelete = getStations();
        assertThat(stationsAfterDelete.size()).isEqualTo(2);
    }

    public void createStation(String name) {
        String path = "/api/stations";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        super.post(params, path);
    }

    public void deleteStation(Long id) {
        String path = "/api/stations/" + id;

        super.delete(path);
    }

    public List<StationResponse> getStations() {
        String path = "/api/stations";

        return super.getList(path, StationResponse.class);
    }
}
