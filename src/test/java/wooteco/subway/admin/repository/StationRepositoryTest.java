package wooteco.subway.admin.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import wooteco.subway.admin.domain.Station;

@DataJdbcTest
public class StationRepositoryTest {
	@Autowired
	private StationRepository stationRepository;

	@Test
	void saveStation() {
		String stationName = "강남역";
		stationRepository.save(new Station(stationName));

		assertThrows(DbActionExecutionException.class, () -> stationRepository.save(new Station(stationName)));
	}

	@Test
	void findAllById() {
		Station station1 = stationRepository.save(new Station("강남역"));
		Station station4 = stationRepository.save(new Station("방배역"));

		assertThat(stationRepository.findAllById(Arrays.asList(station1.getId(), station4.getId()))).isEqualTo(
			Arrays.asList(station1, station4));
	}
}
