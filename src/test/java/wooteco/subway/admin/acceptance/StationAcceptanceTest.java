package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql({"/schema-test.sql","/truncate.sql"})
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 관리한다")
    @Test
    void manageStation() {
        // when
        createStation(강남역);
        createStation(역삼역);
        createStation(선릉역);
        // then
        List<StationResponse> stations = getStations();
        assertThat(stations.size()).isEqualTo(3);

        // when
        deleteStation(stations.get(0).getId());
        // then
        List<StationResponse> stationsAfterDelete = getStations();
        assertThat(stationsAfterDelete.size()).isEqualTo(2);
    }

    StationResponse createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        String path = "/stations";
        return post(path, params, StationResponse.class);
    }

    List<StationResponse> getStations() {
        String path = "/stations";
        return getList(path, StationResponse.class);
    }

    void deleteStation(Long id) {
        String path = "/stations/" + id;
        delete(path);
    }
}
