package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.dto.StationResponse;

public class StationAcceptanceTest extends AcceptanceTest {
	@DisplayName("지하철역을 관리한다")
	@Test
	void manageStation() {
		// when
		createStation(강남);
		createStation(역삼);
		createStation(선릉);
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
