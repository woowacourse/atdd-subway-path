package wooteco.subway.admin.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.admin.domain.Station;

@DataJdbcTest
@Sql("/truncate.sql")
public class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

    @Test
    void saveStation() {
        String stationName = "강남역";
        stationRepository.save(Station.from(stationName));

        assertThrows(DbActionExecutionException.class,
            () -> stationRepository.save(Station.from(stationName)));
    }
}
