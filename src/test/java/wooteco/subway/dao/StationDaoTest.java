package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.선릉역;
import static wooteco.subway.Fixtures.역삼역;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.station.Station;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("새로운 지하철 역을 등록할 수 있다.")
    void save() {
        Station savedStation = stationDao.save(선릉역);

        assertThat(savedStation).isNotNull();
    }

    @Test
    @DisplayName("등록된 지하철 역들을 반환한다.")
    void findAll() {
        stationDao.save(강남역);
        stationDao.save(역삼역);
        stationDao.save(선릉역);

        List<String> actual = stationDao.findAll().stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        List<String> expected = List.of(강남역.getName(), 역삼역.getName(), 선릉역.getName());

        assertThat(actual).containsAll(expected);
    }

    @Test
    @DisplayName("등록된 지하철을 삭제한다.")
    void deleteById() {
        Station savedStation = stationDao.save(선릉역);
        Long id = savedStation.getId();

        stationDao.deleteById(id);

        assertThat(stationDao.findAll()).hasSize(0);
    }
}
