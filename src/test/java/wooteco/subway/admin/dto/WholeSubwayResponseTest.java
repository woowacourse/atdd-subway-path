package wooteco.subway.admin.dto;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;

class WholeSubwayResponseTest {
	@Test
	public void equalsTest() {
		Line line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "red");
		Line line2 = new Line(2L, "25호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "red");
		Station station1 = new Station(1L, "STATION_NAME1");
		Station station2 = new Station(2L, "STATION_NAME2");
		Station station3 = new Station(3L, "STATION_NAME3");
		Station station4 = new Station(4L, "STATION_NAME4");

		LineDetailResponse lineDetailResponse1 = LineDetailResponse.of(line1,
			Arrays.asList(station1, station2, station3));
		LineDetailResponse lineDetailResponse2 = LineDetailResponse.of(line2, Arrays.asList(station4));
		LineDetailResponse lineDetailResponse3 = LineDetailResponse.of(line1,
			Arrays.asList(station1, station2, station3));
		WholeSubwayResponse whole1 = WholeSubwayResponse.of(Arrays.asList(lineDetailResponse1));
		WholeSubwayResponse whole2 = WholeSubwayResponse.of(Arrays.asList(lineDetailResponse2));
		WholeSubwayResponse whole3 = WholeSubwayResponse.of(Arrays.asList(lineDetailResponse3));

		assertThat(whole1).isEqualTo(whole1);
		assertThat(whole1).isNotEqualTo(whole2);
		assertThat(whole1).isEqualTo(whole3);
	}
}