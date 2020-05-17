package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LineTest extends domainTest {
	private Line line;

	@BeforeEach
	void setUp() {
		line = createLine("2호선", 1L);
		line.addLineStation(createLineStation(null, 1L));
		line.addLineStation(createLineStation(1L, 2L));
		line.addLineStation(createLineStation(2L, 3L));
	}

	@Test
	void addLineStation() {
		line.addLineStation(createLineStation(null, 4L));

		assertThat(line.getStations()).hasSize(4);
		LineStation lineStation = line.getStations().stream()
			.filter(
				it -> Objects.nonNull(it.getPreStationId()) && it.getPreStationId() == 4L)
			.findFirst()
			.orElseThrow(RuntimeException::new);
		assertThat(lineStation.getStationId()).isEqualTo(1L);
	}

	@Test
	void getLineStations() {
		List<Long> stationIds = line.getLineStationsId();

		assertThat(stationIds.size()).isEqualTo(3);
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
		assertThat(stationIds.get(2)).isEqualTo(3L);
	}

	@ParameterizedTest
	@ValueSource(longs = {1L, 2L, 3L})
	void removeLineStation(Long stationId) {
		line.removeLineStationById(stationId);

		assertThat(line.getStations()).hasSize(2);
	}
}
