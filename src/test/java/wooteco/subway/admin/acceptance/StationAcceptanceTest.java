package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.response.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 관리한다")
    @Test
    void manageStation() {
        // when : 지하철을 생성한다.
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        // then : 지하철이 생성되었는지 확인한다.
        List<StationResponse> stations = getStations();
        assertThat(stations.size()).isEqualTo(3);

        // when : 지하철을 삭제한다.
        deleteStation(stations.get(0).getId());
        // then : 지하철이 삭제되었는지 확인한다.
        List<StationResponse> stationsAfterDelete = getStations();
        assertThat(stationsAfterDelete.size()).isEqualTo(2);
    }
}
