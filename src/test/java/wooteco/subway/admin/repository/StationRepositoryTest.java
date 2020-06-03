package wooteco.subway.admin.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import wooteco.subway.admin.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJdbcTest
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        String stationName = "강남역";
        stationRepository.save(new Station(stationName));
    }

    @Test
    void saveStation() {
        assertThrows(DbActionExecutionException.class, () -> stationRepository.save(new Station("강남역")));
    }

    @Test
    @DisplayName("역이름을 검색하면 올바른 역 정보를 반환한다.")
    void findByNameTest() {
        Station station = stationRepository.findByName("강남역").orElseThrow(IllegalArgumentException::new);
        assertThat(station.getName()).isEqualTo("강남역");
    }
}
