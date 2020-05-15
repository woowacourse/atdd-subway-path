package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

public class LineStationAcceptanceTest extends AcceptanceTest {

	@DisplayName("지하철 노선에서 지하철역 추가 / 제외")
	@Test
	void manageLineStation() {
		// Given
		createLine("8호선");
		createStation("잠실역");
		createStation("송파역");
		createStation("모란역");
		LineResponse LineEight = getLines().get(0); // 8호선
		StationResponse stationJamSil = getStations().get(0); // 잠실
		StationResponse stationSongPa = getStations().get(1); // 송파
		StationResponse stationMoran = getStations().get(2); // 모란

		// When - Then POP!
		assertThatThrownBy(
			() -> addLineStation(LineEight.getId(), stationMoran.getId(), stationJamSil.getId()));

		// When
		addLineStation(LineEight.getId(), null, stationJamSil.getId());
		// Then
		LineResponse lineResponse = findStationsByLineId(LineEight.getId());
		assertThat(lineResponse.getStations()).isNotNull();

		// When
		addLineStation(LineEight.getId(), stationJamSil.getId(), stationSongPa.getId());
		// Then
		lineResponse = findStationsByLineId(LineEight.getId());
		assertThat(lineResponse.getStations()).hasSize(2);

		// When - Then POP!
		assertThatThrownBy(
			() -> addLineStation(LineEight.getId(), stationJamSil.getId(), stationJamSil.getId()));

		// When
		List<StationResponse> stations = findStationsByLineId(LineEight.getId()).getStations();
		// Then
		assertThat(stations.size()).isNotEqualTo(0);
		assertThat(stations).contains(stationJamSil);

		// When
		removeLineStation(LineEight.getId(), stationJamSil.getId());
		removeLineStation(LineEight.getId(), stationSongPa.getId());
		// Then
		stations = findStationsByLineId(LineEight.getId()).getStations();
		assertThat(stations).doesNotContain(stationJamSil);

		// When
		stations = findStationsByLineId(LineEight.getId()).getStations();
		// Then
		assertThat(stations).isNotNull();
		assertThat(stations).doesNotContain(stationJamSil);
	}
}
