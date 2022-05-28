package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.선릉역;
import static wooteco.subway.Fixtures.역삼역;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.entity.StationEntity;

@JdbcTest
class StationDaoTest {

    private final StationDao stationDao;

    @Autowired
    public StationDaoTest(JdbcTemplate jdbcTemplate) {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("새로운 지하철 역을 등록할 수 있다.")
    void save() {
        // given
        StationEntity stationEntity = StationEntity.from(강남역);

        // when
        Long savedId = stationDao.save(stationEntity);

        // then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("등록된 지하철 역들을 반환한다.")
    void findAll() {
        // given
        stationDao.save(StationEntity.from(강남역));
        stationDao.save(StationEntity.from(역삼역));
        stationDao.save(StationEntity.from(선릉역));

        // when
        List<String> actual = stationDao.findAll().stream()
                .map(StationEntity::getName)
                .collect(Collectors.toList());
        List<String> expected = List.of(강남역.getName(), 역삼역.getName(), 선릉역.getName());

        // then
        assertThat(actual).containsAll(expected);
    }

    @Test
    @DisplayName("등록된 지하철을 삭제한다.")
    void deleteById() {
        // given
        Long savedId = stationDao.save(StationEntity.from(선릉역));

        // when
        stationDao.deleteById(savedId);

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }
}
