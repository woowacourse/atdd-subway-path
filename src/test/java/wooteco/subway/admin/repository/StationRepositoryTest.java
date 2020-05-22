package wooteco.subway.admin.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import wooteco.subway.admin.domain.Station;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJdbcTest
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

    @DisplayName("같은 이름의 역이 중복되어 저장될 경우 예외처리한다.")
    @Test
    void saveStation() {
        String stationName = "강남역";
        stationRepository.save(new Station(stationName));

        assertThrows(DbActionExecutionException.class, () -> stationRepository.save(new Station(stationName)));
    }
}
