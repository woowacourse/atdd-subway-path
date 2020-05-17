package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.station.service.dto.StationResponse;

public class StationAcceptanceTest extends AcceptanceTest {
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
}
