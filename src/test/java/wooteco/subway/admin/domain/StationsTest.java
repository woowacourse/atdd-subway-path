package wooteco.subway.admin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StationsTest {

	@DisplayName("List<Station>을 받으면 Map으로 저장한다.")
	@Test
	void create() {
		List<Station> sourceStations = new ArrayList<>();
		Stations stations = new Stations(sourceStations);

		assertThat(stations).isInstanceOf(Stations.class);
		assertThat(stations).isNotNull();
	}

	@DisplayName("findByKey id를 입력하면 해당 Station을 반환한다.")
	@Test
	void findByKey_ReturnStationValue() {
		Station station1 = new Station(1L, "구로");
		Station station2 = new Station(2L, "신도림");
		Station station3 = new Station(3L, "신길");
		Station station4 = new Station(4L, "용산");
		List<Station> sourceStations = Arrays.asList(station1, station2, station3, station4);
		Stations stations = new Stations(sourceStations);
		Long stationId = 1L;
		Station station = stations.findByKey(stationId);

		assertEquals(station.getName(), "구로");
	}
}