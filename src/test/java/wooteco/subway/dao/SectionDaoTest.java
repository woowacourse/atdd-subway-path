package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class SectionDaoTest extends DatabaseUsageTest {

    private static final Station STATION1 = new Station(1L, "강남역");
    private static final Station STATION2 = new Station(2L, "선릉역");
    private static final Station STATION3 = new Station(3L, "잠실역");
    private static final Station STATION4 = new Station(4L, "강변역");
    private static final Station STATION5 = new Station(5L, "청계산입구역");

    @Autowired
    private SectionDao dao;

    @BeforeEach
    void setup() {
        databaseFixtureUtils.saveStations(STATION1, STATION2, STATION3, STATION4, STATION5);
        databaseFixtureUtils.saveLine("1호선", "색깔");
        databaseFixtureUtils.saveLine("2호선", "색깔2");
    }

    @DisplayName("findAll 메서드들은 조건에 부합하는 모든 데이터를 조회한다")
    @Nested
    class FindAllMethodsTest {

        @BeforeEach
        void setup() {
            databaseFixtureUtils.saveSection(1L, STATION1, STATION2, 20);
            databaseFixtureUtils.saveSection(1L, STATION2, STATION3, 10);
            databaseFixtureUtils.saveSection(2L, STATION1, STATION3, 30);
        }

        @Test
        void findAll_메서드는_모든_구간_데이터를_조회() {
            List<Section> actual = dao.findAll();
            List<Section> expected = List.of(
                    new Section(1L, STATION1, STATION2, 20),
                    new Section(1L, STATION2, STATION3, 10),
                    new Section(2L, STATION1, STATION3, 30));

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void findAllByLineId_메서드는_lineId에_해당하는_모든_구간_데이터를_조회() {
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(
                    new Section(1L, STATION1, STATION2, 20),
                    new Section(1L, STATION2, STATION3, 10));

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void findAllByStationId_메서드는_특정_역이_등록된_모든_구간들의_데이터를_조회() {
            List<Section> actual = dao.findAllByStationId(1L);
            List<Section> expected = List.of(
                    new Section(1L, STATION1, STATION2, 20),
                    new Section(2L, STATION1, STATION3, 30));

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    @DisplayName("save 메서드는 데이터를 저장한다")
    @Nested
    class SaveTest {

        @Test
        void 중복되지_않는_정보인_경우_데이터_생성() {
            dao.save(new Section(1L, STATION1, STATION3, 10));

            boolean created = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM section "
                    + "WHERE id = 1 AND line_id = 1 AND up_station_id = 1 "
                    + "AND down_station_id = 3 AND distance = 10", Integer.class) > 0;

            assertThat(created).isTrue();
        }

        @Test
        void 중복되는_정보로_생성하려는_경우_예외발생() {
            databaseFixtureUtils.saveSection(1L, STATION1, STATION2, 10);

            Section existingSection = new Section(1L, STATION1, STATION2, 10);
            assertThatThrownBy(() -> dao.save(existingSection))
                    .isInstanceOf(DataAccessException.class);
        }
    }

    @DisplayName("delete 메서드는 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        @Test
        void delete_메서드는_노선과_상행역_하행역에_부합하는_데이터를_삭제() {
            databaseFixtureUtils.saveSection(1L, STATION1, STATION3, 10);

            dao.delete(new Section(1L, STATION1, STATION3, 10));
            boolean exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM section WHERE line_id = 1", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void 거리_정보가_틀리더라도_성공적으로_데이터_삭제_성공() {
            databaseFixtureUtils.saveSection(1L, STATION1, STATION3, 10);

            dao.delete(new Section(1L, STATION1, STATION3, 99999999));
            boolean exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM section WHERE line_id = 2", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void 존재하지_않는_구간_정보가_입력되더라도_결과는_동일하므로_예외_미발생() {
            Section nonExistingSection = new Section(99999L, STATION1, STATION2, 10);
            assertThatNoException()
                    .isThrownBy(() -> dao.delete(nonExistingSection));
        }
    }

    @Test
    void deleteAllByLineId_메서드는_노선에_해당되는_모든_구간_데이터를_삭제() {
        databaseFixtureUtils.saveSection(1L, STATION2, STATION3, 10);
        databaseFixtureUtils.saveSection(1L, STATION1, STATION2, 5);

        dao.deleteAllByLineId(1L);
        boolean exists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM section WHERE line_id = 1", Integer.class) > 0;

        assertThat(exists).isFalse();
    }
}
