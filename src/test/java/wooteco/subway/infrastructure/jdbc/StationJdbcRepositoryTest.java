package wooteco.subway.infrastructure.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.TestFixture.강남역;
import static wooteco.subway.TestFixture.역삼역;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infrastructure.jdbc.dao.StationDao;

@DisplayName("지하철역 Repository")
@JdbcTest
class StationJdbcRepositoryTest {

    @Autowired
    private DataSource dataSource;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(dataSource);
        this.stationRepository = new StationJdbcRepository(stationDao);
    }

    @DisplayName("역을 생성한다.")
    @Test
    void save() {
        long actual = (stationRepository.save(강남역)).getId();
        assertThat(actual).isGreaterThan(0L);
    }

    @DisplayName("역 목록을 조회한다.")
    @Test
    void getAll() {
        List<Station> expected = List.of(강남역, 역삼역);
        expected.forEach(stationRepository::save);

        List<Station> actual = stationRepository.getAll();
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @DisplayName("역을 조회한다.")
    @Test
    void getById() {
        Station station = stationRepository.save(강남역);
        Station actual = stationRepository.getById(station.getId());

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(station);
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void remove() {
        Station station = stationRepository.save(강남역);
        stationRepository.remove(station.getId());

        List<Station> actual = stationRepository.getAll();
        assertThat(actual).isEmpty();
    }

    @DisplayName("식별자에 해당하는 역이 존재하는지 확인한다.")
    @Test
    void existsById() {
        Station station = stationRepository.save(강남역);

        boolean actual = stationRepository.existsById(station.getId());
        assertThat(actual).isTrue();
    }

    @DisplayName("이름에 해당하는 역이 존재하는지 확인한다.")
    @Test
    void existsByName() {
        Station station = stationRepository.save(강남역);

        boolean actual = stationRepository.existsByName(station.getName());
        assertThat(actual).isTrue();
    }
}