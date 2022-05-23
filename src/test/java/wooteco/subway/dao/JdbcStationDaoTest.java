package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.dao.JdbcStationDao;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.StationEntity;

@JdbcTest
class JdbcStationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new JdbcStationDao(jdbcTemplate);

        List<StationEntity> stationEntities = stationDao.findAll();
        List<Long> stationIds = stationEntities.stream()
            .map(StationEntity::getId)
            .collect(Collectors.toList());

        for (Long stationId : stationIds) {
            stationDao.deleteById(stationId);
        }
    }

    @Test
    void save() {
        // given
        Station Station = new Station("범고래");

        // when
        Long result = stationDao.save(Station);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void findAll() {
        // given
        Long savedId1 = stationDao.save(new Station("범고래"));
        Long savedId2 = stationDao.save(new Station("애쉬"));

        // when
        List<StationEntity> stations = stationDao.findAll();

        // then
        assertAll(
            () -> assertThat(stations.size()).isEqualTo(2),
            () -> assertThat(savedId1).isNotNull(),
            () -> assertThat(savedId2).isNotNull()
        );
    }

    @Test
    void validateDuplication() {
        // given
        Station Station1 = new Station("범고래");
        Station Station2 = new Station("범고래");

        // when
        stationDao.save(Station1);

        // then
        assertThatThrownBy(() -> stationDao.save(Station2))
            .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void delete() {
        // given
        Station station = new Station("범고래");
        Long savedId = stationDao.save(station);

        // when
        stationDao.deleteById(savedId);
        List<StationEntity> stationEntities = stationDao.findAll();

        // then
        StationEntity stationEntity = new StationEntity(savedId, station.getName());
        assertThat(stationEntities).doesNotContain(stationEntity);
    }
}
