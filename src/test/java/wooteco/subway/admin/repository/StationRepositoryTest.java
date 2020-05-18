package wooteco.subway.admin.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
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
    @Disabled("지하철 역 저장")
    void saveStation() {
        String stationName = "강남역";
        stationRepository.save(new Station(stationName));

        assertThrows(DbActionExecutionException.class,
            () -> stationRepository.save(new Station(stationName)));
    }
}
