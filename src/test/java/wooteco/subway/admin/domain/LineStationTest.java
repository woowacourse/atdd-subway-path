package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class LineStationTest {

	@Test
	void findMatchingStation() {
		LineStation lineStation = new LineStation(null, 1L, 10, 10);

		Station sampleStation1 = new Station(1L, "가역");
		Station sampleStation2 = new Station(2L, "나역");
		Station sampleStation3 = new Station(3L, "다역");
		Station sampleStation4 = new Station(4L, "라역");

		List<Station> stations = Arrays.asList(
			sampleStation1,
			sampleStation2,
			sampleStation3,
			sampleStation4
		);
		assertThat(lineStation.findMatchingStation(stations)).isEqualTo(sampleStation1);
	}
}