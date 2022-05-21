package wooteco.subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
class StationDaoTest {

    private final StationDao stationDao;

    StationDaoTest(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @BeforeEach
    void setUp() {

    }

    @DisplayName("")
    @Test
    void queryById() {

    }
}
