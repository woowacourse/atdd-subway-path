package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import wooteco.subway.domain.line.Line;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class LineDaoTest extends DatabaseUsageTest {

    @Autowired
    private LineDao dao;

    @Test
    void findAll_메서드는_존재하는_모든_노선_정보들의_리스트를_반환() {
        databaseFixtureUtils.saveLine("노선명1", "색깔1", 100);
        databaseFixtureUtils.saveLine("노선명2", "색깔2", 0);
        databaseFixtureUtils.saveLine("노선명3", "색깔3", 300);

        List<Line> actual = dao.findAll();
        List<Line> expected = List.of(
                new Line(1L, "노선명1", "색깔1", 100),
                new Line(2L, "노선명2", "색깔2", 0),
                new Line(3L, "노선명3", "색깔3", 300));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("findById 메서드는 특정 id의 데이터를 조회한다.")
    @Nested
    class FindByIdTest {

        @Test
        void 존재하는_데이터인_경우_해당_데이터가_담긴_Optional_반환() {
            databaseFixtureUtils.saveLine("존재하는 노선명", "색깔", 100);

            Line actual = dao.findById(1L).get();
            Line expected = new Line(1L, "존재하는 노선명", "색깔", 100);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_데이터의_id인_경우_비어있는_Optional_반환() {
            boolean dataFound = dao.findById(9999L).isPresent();

            assertThat(dataFound).isFalse();
        }
    }

    @Test
    void findAllByIds_메서드는_id_목록에_해당되는_모든_데이터를_조회() {
        databaseFixtureUtils.saveLine("노선명1", "색깔1", 100);
        databaseFixtureUtils.saveLine("노선명2", "색깔2", 0);
        databaseFixtureUtils.saveLine("노선명3", "색깔3", 300);

        List<Line> actual = dao.findAllByIds(List.of(1L, 3L));
        List<Line> expected = List.of(
                new Line(1L, "노선명1", "색깔1", 100),
                new Line(3L, "노선명3", "색깔3", 300));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("findByName 메서드는 name에 해당하는 데이터를 조회한다")
    @Nested
    class FindByNameTest {

        @Test
        void 저장된_name인_경우_해당_데이터가_담긴_Optional_반환() {
            databaseFixtureUtils.saveLine("존재하는 노선명", "색깔", 100);

            Line actual = dao.findByName("존재하는 노선명").get();
            Line expected = new Line(1L, "존재하는 노선명", "색깔", 100);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 저장되지_않는_name인_경우_비어있는_Optional_반환() {
            boolean dataFound = dao.findByName("존재하지 않는 역 이름").isPresent();

            assertThat(dataFound).isFalse();
        }
    }

    @DisplayName("save 메서드는 데이터를 저장한다")
    @Nested
    class SaveTest {

        @Test
        void 중복되지_않는_이름인_경우_성공() {
            databaseFixtureUtils.saveLine("존재하는 노선명1", "색깔");
            databaseFixtureUtils.saveLine("존재하는 노선명2", "색깔");

            Line actual = dao.save(new Line("새로운 노선명", "색깔", 900));
            Line expected = new Line(3L, "새로운 노선명", "색깔", 900);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 중복되는_이름인_경우_예외발생() {
            databaseFixtureUtils.saveLine("중복되는 노선명", "색깔");

            assertThatThrownBy(() -> dao.save(new Line("중복되는 노선명", "다른 색깔", 0)))
                    .isInstanceOf(DataAccessException.class);
        }
    }

    @DisplayName("update 메서드는 데이터를 수정한다")
    @Nested
    class UpdateTest {

        @Test
        void 중복되지_않는_이름으로_수정_가능() {
            databaseFixtureUtils.saveLine("현재 노선명", "색깔은 그대로", 100);

            dao.update(new Line(1L, "새로운 노선 이름", "색깔은 그대로", 100));
            String actual = jdbcTemplate.queryForObject("SELECT name FROM line WHERE id = 1", String.class);
            String expected = "새로운 노선 이름";

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 색상은_자유롭게_수정_가능() {
            databaseFixtureUtils.saveLine("노선명 그대로", "현재 색깔", 1000);
            databaseFixtureUtils.saveLine("노선명2", "중복되는 색깔", 0);

            dao.update(new Line(1L, "노선명 그대로", "중복되는 색깔", 1000));
            String actual = jdbcTemplate.queryForObject("SELECT color FROM line WHERE id = 1", String.class);
            String expected = "중복되는 색깔";

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 추가_비용은_자유롭게_수정_가능() {
            databaseFixtureUtils.saveLine("노선명 그대로", "색깔 그대로", 1000);

            dao.update(new Line(1L, "노선명 그대로", "색깔 그대로", 3000));
            int actual = jdbcTemplate.queryForObject("SELECT extra_fare FROM line WHERE id = 1", Integer.class);
            int expected = 3000;

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 중복되는_이름으로_수정하려는_경우_예외발생() {
            databaseFixtureUtils.saveLine("현재 노선명", "색깔은 그대로");
            databaseFixtureUtils.saveLine("존재하는 노선명", "색깔");

            assertThatThrownBy(() -> dao.update(new Line(1L, "존재하는 노선명", "색깔은 그대로", 100)))
                    .isInstanceOf(DataAccessException.class);
        }

        @Test
        void 존재하지_않는_노선을_수정하려는_경우_예외_미발생() {
            assertThatNoException()
                    .isThrownBy(() -> dao.update(new Line(999999999L, "새로운 노선 이름", "노란색", 100)));
        }

        @Test
        void 기존_정보와_동일하게_수정하려는_경우_결과는_동일하므로_예외_미발생() {
            databaseFixtureUtils.saveLine("노선명 그대로", "색깔 그대로", 1000);

            assertThatNoException()
                    .isThrownBy(() -> dao.update(new Line(1L, "노선명 그대로", "색깔 그대로", 3000)));
        }
    }

    @DisplayName("deleteById 메서드는 특정 데이터를 삭제한다")
    @Nested
    class DeleteByIdTest {

        @Test
        void 존재하는_데이터의_id가_입력된_경우_삭제성공() {
            databaseFixtureUtils.saveLine("존재하는 노선", "색깔");
            dao.deleteById(1L);

            boolean exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM line WHERE id = 1", Integer.class) > 0;

            assertThat(exists).isFalse();
        }

        @Test
        void 존재하지_않는_데이터의_id가_입력되더라도_결과는_동일하므로_예외_미발생() {
            assertThatNoException()
                    .isThrownBy(() -> dao.deleteById(99999L));
        }
    }
}
