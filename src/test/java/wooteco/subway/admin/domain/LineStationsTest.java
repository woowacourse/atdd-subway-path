package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.jdbc.Sql;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
@Sql("/truncate.sql")
public class LineStationsTest {
	private LineStations lineStations;

	@BeforeEach
	void setUp() {
		lineStations = new LineStations();
		lineStations.addLineStation(new LineStation(null, 1L, 10, 10));
		lineStations.addLineStation(new LineStation(1L, 2L, 10, 10));
		lineStations.addLineStation(new LineStation(2L, 3L, 10, 10));
	}

	@Test
	void addLineStation() {
		lineStations.addLineStation(new LineStation(null, 4L, 10, 10));

		assertThat(lineStations.getStations()).hasSize(4);
		LineStation lineStation = lineStations.getStations().stream()
			.filter(it -> Objects.equals(it.getPreStationId(), 4L))
			.findFirst()
			.orElseThrow(RuntimeException::new);
		assertThat(lineStation.getStationId()).isEqualTo(1L);
	}

	@Test
	void getLineStations() {
		List<Long> stationIds = lineStations.getLineStationsId();

		assertThat(stationIds.size()).isEqualTo(3);
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
		assertThat(stationIds.get(2)).isEqualTo(3L);
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L, 3L})
	void removeLineStation(Long stationId) {
		lineStations.removeLineStationById(stationId);

		assertThat(lineStations.getStations()).hasSize(2);
	}
}
