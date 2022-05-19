package wooteco.subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.station.Station;

import javax.sql.DataSource;

@JdbcTest
@Sql("classpath:truncate.sql")
public class StationDaoTest {

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setUp(){
        stationDao = new StationDao(dataSource);
        stationDao.insert(new Station("강남역"));
    }


}
