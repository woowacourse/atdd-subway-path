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
import wooteco.subway.entity.LineEntity;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class SectionDaoTest extends DatabaseUsageTest {

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 선릉역 = new Station(2L, "선릉역");
    private static final Station 잠실역 = new Station(3L, "잠실역");
    private static final Station 강변역 = new Station(4L, "강변역");
    private static final Station 청계산입구역 = new Station(5L, "청계산입구역");

    @Autowired
    private SectionDao dao;

    @BeforeEach
    void setup() {
        databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역, 강변역, 청계산입구역);
        LineEntity 신분당선 = new LineEntity("신분당선", "색깔", 0);
        LineEntity 수인선 = new LineEntity("수인선", "색깔2", 0);
        databaseFixtureUtils.saveLines(신분당선, 수인선);
    }

    @DisplayName("findAll 메서드들은 조건에 부합하는 모든 데이터를 조회한다")
    @Nested
    class FindAllMethodsTest {

        private final Section 강남_선릉 = new Section(1L, 강남역, 선릉역, 20);
        private final Section 선릉_잠실 = new Section(1L, 선릉역, 잠실역, 10);
        private final Section 강남_잠실 = new Section(2L, 강남역, 잠실역, 30);

        @BeforeEach
        void setup() {
            databaseFixtureUtils.saveSections(강남_선릉, 선릉_잠실, 강남_잠실);
        }

        @Test
        void findAll_메서드는_모든_구간_데이터를_조회() {
            List<Section> actual = dao.findAll();
            List<Section> expected = List.of(강남_선릉, 선릉_잠실, 강남_잠실);

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void findAllByLineId_메서드는_lineId에_해당하는_모든_구간_데이터를_조회() {
            List<Section> actual = dao.findAllByLineId(1L);
            List<Section> expected = List.of(강남_선릉, 선릉_잠실);

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }

        @Test
        void findAllByStationId_메서드는_특정_역이_등록된_모든_구간들의_데이터를_조회() {
            List<Section> actual = dao.findAllByStationId(1L);
            List<Section> expected = List.of(강남_선릉, 강남_잠실);

            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        }
    }

    @DisplayName("save 메서드는 복수의 데이터를 저장한다")
    @Nested
    class SaveTest {

        @Test
        void 중복되지_않는_정보인_경우_데이터_생성() {
            dao.save(List.of(new Section(1L, 강남역, 잠실역, 10),
                    new Section(1L, 잠실역, 강변역, 20)));

            int createdData = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM section "
                    + "WHERE line_id = 1", Integer.class);

            assertThat(createdData).isEqualTo(2);
        }

        @Test
        void 중복되는_정보로_생성하려는_경우_예외발생() {
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);

            Section existingSection = new Section(1L, 강남역, 선릉역, 10);
            assertThatThrownBy(() -> dao.save(List.of(existingSection)))
                    .isInstanceOf(DataAccessException.class);
        }
    }

    @DisplayName("delete 메서드는 복수의 데이터를 삭제한다")
    @Nested
    class DeleteTest {

        @Test
        void delete_메서드는_노선과_상행역_하행역에_부합하는_데이터들을_삭제() {
            saveSectionTestFixture(1L, 강남역, 잠실역, 10);
            saveSectionTestFixture(1L, 잠실역, 강변역, 10);

            dao.delete(List.of(new Section(1L, 강남역, 잠실역, 10)));
            int existingSectionCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM section WHERE line_id = 1", Integer.class);

            assertThat(existingSectionCount).isEqualTo(1);
        }

        @Test
        void 거리_정보가_틀리더라도_성공적으로_데이터_삭제_성공() {
            saveSectionTestFixture(1L, 강남역, 잠실역, 10);

            dao.delete(List.of(new Section(1L, 강남역, 잠실역, 99999999)));
            boolean exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM section WHERE line_id = 2", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void 존재하지_않는_구간_정보가_입력되더라도_결과는_동일하므로_예외_미발생() {
            Section nonExistingSection = new Section(99999L, 강남역, 선릉역, 10);
            assertThatNoException()
                    .isThrownBy(() -> dao.delete(List.of(nonExistingSection)));
        }
    }

    @Test
    void deleteAllByLineId_메서드는_노선에_해당되는_모든_구간_데이터를_삭제() {
        saveSectionTestFixture(1L, 선릉역, 잠실역, 10);
        saveSectionTestFixture(1L, 강남역, 선릉역, 5);

        dao.deleteAllByLineId(1L);
        boolean exists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM section WHERE line_id = 1", Integer.class) > 0;

        assertThat(exists).isFalse();
    }

    private void saveSectionTestFixture(Long lineId, Station upStation, Station downStation, int distance) {
        databaseFixtureUtils.saveSections(new Section(lineId, upStation, downStation, distance));
    }
}
