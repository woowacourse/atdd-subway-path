package wooteco.subway.station.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class StationDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(dataSource);
        jdbcTemplate.execute("create table if not exists STATION ( id bigint auto_increment not null, name varchar(255) not null unique, primary key(id))");
    }

    @DisplayName("id 리스트를 전달하면 해당 아이디에 부합하는 엔티티들을 반환한다.")
    @Test
    void findByIds() {
        Station first = stationDao.insert(new Station("1번역"));
        Station second = stationDao.insert(new Station("2번역"));
        Station third = stationDao.insert(new Station("3번역"));

        List<String> stationNames = stationDao.findByIds(Arrays.asList(first.getId(), second.getId(), third.getId()))
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactly("1번역", "2번역", "3번역");
    }
}
