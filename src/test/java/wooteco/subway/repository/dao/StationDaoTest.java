package wooteco.subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixture.DAE_LIM_ENTITY;
import static wooteco.subway.TestFixture.GANG_NAM_ENTITY;
import static wooteco.subway.TestFixture.SAM_SUNG_ENTITY;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import wooteco.subway.repository.dao.entity.StationEntity;

@TestConstructor(autowireMode = AutowireMode.ALL)
@JdbcTest
class StationDaoTest {

    private final StationDao stationDao;

    public StationDaoTest(DataSource dataSource) {
        this.stationDao = new StationDao(dataSource);
    }

    @DisplayName("지하철역을 저장한다")
    @Test
    void save() {
        // given
        final StationEntity stationEntity = GANG_NAM_ENTITY;

        // when
        final Long generatedId = stationDao.save(stationEntity);

        // then
        assertThat(generatedId).isGreaterThan(0L);
    }

    @DisplayName("지하철역 전체 목록을 조회한다")
    @Test
    void findAll() {
        // given
        final StationEntity gangNamEntity = GANG_NAM_ENTITY;
        final StationEntity daeLimEntity = DAE_LIM_ENTITY;
        final StationEntity samSungEntity = SAM_SUNG_ENTITY;

        for (StationEntity stationEntity : List.of(gangNamEntity, daeLimEntity, samSungEntity)) {
            stationDao.save(stationEntity);
        }

        // when
        final List<StationEntity> stations = stationDao.findAll();

        // then
        assertAll(
                () -> assertThat(stations.size()).isEqualTo(3),
                () -> assertThat(stations).extracting("name")
                        .containsExactly(GANG_NAM_ENTITY.getName(), DAE_LIM_ENTITY.getName(), SAM_SUNG_ENTITY.getName())
        );
    }

    @DisplayName("ID로 지하철역 1개를 조회한다")
    @Test
    void findById() {
        // given
        final StationEntity stationEntity = GANG_NAM_ENTITY;
        final Long savedId = stationDao.save(stationEntity);

        // when
        final Optional<StationEntity> foundById = stationDao.findById(savedId);

        // then
        assertAll(
                () -> assertThat(foundById).isPresent(),
                () -> assertThat(foundById.get()).usingRecursiveComparison()
                        .isEqualTo(new StationEntity(savedId, GANG_NAM_ENTITY.getName()))
        );
    }

    @DisplayName("존재하지 않는 역 ID로 지하철역을 조회하면 비어있는 결과가 반환된다")
    @Test
    void findWithNonexistentId() {
        // given & when
        final Optional<StationEntity> notExistEntity = stationDao.findById(1L);

        // then
        assertThat(notExistEntity).isEmpty();
    }

    @DisplayName("ID로 지하철역을 삭제할 수 있다")
    @Test
    void deleteById() {
        // given
        final Long generatedId = stationDao.save(GANG_NAM_ENTITY);
        final Optional<StationEntity> beforeDelete = stationDao.findById(generatedId);

        // when
        stationDao.deleteById(generatedId);
        final Optional<StationEntity> afterDelete = stationDao.findById(generatedId);

        // then
        assertAll(
                () -> assertThat(beforeDelete).isPresent(),
                () -> assertThat(afterDelete).isEmpty()
        );
    }

    @DisplayName("역 이름으로 지하철역 존재 여부를 확인할 수 있다")
    @Test
    void existsByName() {
        // given
        final boolean beforeCreation = stationDao.existsByName(GANG_NAM_ENTITY.getName());

        // when
        stationDao.save(GANG_NAM_ENTITY);
        final boolean afterCreation = stationDao.existsByName(GANG_NAM_ENTITY.getName());

        // then
        assertAll(
                () -> assertThat(beforeCreation).isFalse(),
                () -> assertThat(afterCreation).isTrue()
        );
    }

    @DisplayName("ID로 지하철역 존재 여부를 확인할 수 있다")
    @Test
    void existsByID() {
        // given
        final Long generatedId = stationDao.save(GANG_NAM_ENTITY);

        // when
        final boolean afterCreation = stationDao.existsById(generatedId);

        // then
        assertThat(afterCreation).isTrue();
    }
}
