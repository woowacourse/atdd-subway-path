package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.domain.Station;

@JdbcTest
class StationDaoTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 선릉역 = new Station("선릉역");
    private static final Station 잠실역 = new Station("잠실역");

    private final StationDao stationDao;

    @Autowired
    public StationDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("지하철 역을 저장한다.")
    @Test
    void save() {
        final Station station = stationDao.save(강남역);

        assertThat(station.getName()).isEqualTo(강남역.getName());
    }

    @DisplayName("같은 이름의 지하철 역을 저장하는 경우 예외가 발생한다.")
    @Test
    void saveExistingName() {
        stationDao.save(강남역);

        assertThatThrownBy(() -> stationDao.save(강남역))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 역을 조회한다.")
    @Test
    void findAll() {
        stationDao.save(강남역);
        stationDao.save(선릉역);
        stationDao.save(잠실역);

        assertThat(stationDao.findAll().size()).isEqualTo(3);
    }

    @DisplayName("id로 지하철 역을 조회한다.")
    @Test
    void findById() {
        final Station station = stationDao.save(강남역);

        assertThat(stationDao.findById(station.getId()).getName()).isEqualTo(강남역.getName());
    }

    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteById() {
        final Station station = stationDao.save(강남역);

        stationDao.deleteById(station.getId());

        assertThat(stationDao.findAll().size()).isZero();
    }
}
