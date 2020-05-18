package wooteco.subway.admin.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import wooteco.subway.admin.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void findAllNameById() {
        Station station1 = stationRepository.save(new Station("강남역"));
        Station station2 = stationRepository.save(new Station("역삼역"));

        List<String> result = stationRepository.findAllNameById(Arrays.asList(station1.getId(), station2.getId()));
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void saveDuplicatedLine() {
        Station station1 = new Station("잠실역");
        Station station2 = new Station("잠실역");

        stationRepository.save(station1);
        assertThatThrownBy(() -> stationRepository.save(station2))
                .isInstanceOf(DbActionExecutionException.class)
                .hasCauseInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void saveNullLine() {
        Station station = new Station(null);
        assertThatThrownBy(() -> stationRepository.save(station))
                .isInstanceOf(DbActionExecutionException.class)
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }
}
