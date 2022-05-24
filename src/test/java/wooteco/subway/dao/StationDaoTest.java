package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Station;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;
    private Station station;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        station = stationDao.save("선릉역");
    }

    @Test
    void createStation() {
        assertThat(station.getName()).isEqualTo("선릉역");
    }

    @Test
    void findAll() {
        var station2 = stationDao.save("잠실역");

        assertAll(
                () -> assertThat(stationDao.findAll()).hasSize(2),
                () -> assertThat(stationDao.findAll()).contains(station),
                () -> assertThat(stationDao.findAll()).contains(station2)
        );
    }

    @Test
    void deleteById() {
        stationDao.deleteById(station.getId());
        var stations = stationDao.findAll();

        assertThat(stations).doesNotContain(station);
    }

    @Test
    void findById() {
        assertThat(stationDao.findById(station.getId())).isEqualTo(new Station(station.getId(), "선릉역"));
    }

    @Test
    void findByInvalidId() {
        assertThatThrownBy(() -> stationDao.findById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }
}