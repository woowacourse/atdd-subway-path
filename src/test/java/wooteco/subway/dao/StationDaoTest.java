package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.station.StationNotFoundException;

@JdbcTest
@Sql("classpath:truncate.sql")
public class StationDaoTest {

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(dataSource);
        stationDao.insert(new Station("서울역"));
    }

    @DisplayName("이름값을 받아 해당 이름값을 가진 역이 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"서울역, true", "선릉역, false"})
    void existByName(String name, boolean expected) {
        boolean actual = stationDao.existByName(name);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("저장되어있는 모든 지하철 역을 반환한다.")
    @Test
    void findAll() {
        stationDao.insert(new Station("강남역"));

        List<Long> stationIds = stationDao.findAll()
                .stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        assertThat(stationIds)
                .contains(1L, 2L);
    }

    @DisplayName("id에 해당하는 역이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void getById_exception() {
        assertThatThrownBy(() -> stationDao.getById(-1L))
                .isInstanceOf(StationNotFoundException.class);
    }


    @DisplayName("id 값에 해당하는 지하철 역을 삭제한다.")
    @Test
    void deleteById() {
        Long seoulStationId = 1L;
        stationDao.deleteById(seoulStationId);

        List<Station> stations = stationDao.findAll();

        assertThat(stations).isEmpty();
    }

    @DisplayName("지하철 역 아이디 리스트에 해당하는 지하철 역을 모두 반환한다.")
    @Test
    void findStationByIds() {
        stationDao.insert(new Station("강남역"));
        stationDao.insert(new Station("신촌역"));
        stationDao.insert(new Station("아현역"));

        List<Long> stationIds = List.of(1L, 2L);

        List<Station> stations = stationDao.findByIdIn(stationIds);

        assertThat(stations.get(0).getName()).isEqualTo("서울역");
        assertThat(stations.get(1).getName()).isEqualTo("강남역");
    }
}
