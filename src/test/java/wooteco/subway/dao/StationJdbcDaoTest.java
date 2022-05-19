package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Station;

class StationJdbcDaoTest extends DaoImplTest {

    private StationJdbcDao stationJdbcDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationJdbcDao = new StationJdbcDao(jdbcTemplate);
    }

    @DisplayName("역 정보를 저장한다.")
    @Test
    void save() {
        Station station = new Station("역삼역");
        Station newStation = stationJdbcDao.save(station);

        assertThat(newStation.getName()).isEqualTo("역삼역");
    }

    @DisplayName("역 정보들을 가져온다.")
    @Test
    void findAll() {
        List<Station> stations = stationJdbcDao.findAll();

        assertThat(stations.size()).isEqualTo(3);
    }

    @DisplayName("역 정보를 삭제한다.")
    @Test
    void delete() {
        Station station = new Station("역삼역");
        Station newStation = stationJdbcDao.save(station);

        assertThat(stationJdbcDao.deleteById(newStation.getId())).isEqualTo(1);
    }

    @DisplayName("저장된 이름이 있는지 확인한다.")
    @Test
    void existSavedName() {
        String name = "교대역";
        stationJdbcDao.save(new Station(name));

        boolean existByName = stationJdbcDao.existByName(name);

        assertThat(existByName).isTrue();
    }

    @DisplayName("이름을 이용하여 삭제한 후, 다시 저장되는지 확인한다.")
    @Test
    void saveDeletedName() {
        String name = "교대역";
        stationJdbcDao.save(new Station(name));

        stationJdbcDao.deleteByExistName(name);

        assertThat(stationJdbcDao.save(new Station(name)).getName()).isEqualTo(name);
    }
}
