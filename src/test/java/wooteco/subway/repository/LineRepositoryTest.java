package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineMap;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class LineRepositoryTest extends DatabaseUsageTest {

    @Autowired
    private LineRepository repository;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    private static final Station STATION1 = new Station(1L, "강남역");
    private static final Station STATION2 = new Station(2L, "잠실역");

    @BeforeEach
    void setup() {
        databaseFixtureUtils.saveStations("강남역", "잠실역", "선릉역", "청계산입구역");
    }

    @Test
    void findAllLines_메서드는_모든_노선_정보들을_조회하여_도메인들의_리스트로_반환() {
        databaseFixtureUtils.saveLine("노선명1", "색깔1", 1000);
        databaseFixtureUtils.saveLine("노선명2", "색깔2", 0);
        databaseFixtureUtils.saveLine("노선명3", "색깔3", 900);

        List<Line> actual = repository.findAllLines();
        List<Line> expected = List.of(
                new Line(1L, "노선명1", "색깔1", 1000),
                new Line(2L, "노선명2", "색깔2", 0),
                new Line(3L, "노선명3", "색깔3", 900));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllLinesByIds_메서드는_id_목록에_해당되는_모든_노선_정보들을_조회하여_도메인들의_리스트로_반환() {
        databaseFixtureUtils.saveLine("노선명1", "색깔1", 1000);
        databaseFixtureUtils.saveLine("노선명2", "색깔2", 0);
        databaseFixtureUtils.saveLine("노선명3", "색깔3", 900);

        List<Line> actual = repository.findAllLinesByIds(List.of(1L, 3L));
        List<Line> expected = List.of(
                new Line(1L, "노선명1", "색깔1", 1000),
                new Line(3L, "노선명3", "색깔3", 900));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("findExistingLine 메서드는 id에 대응되는 노선을 조회")
    @Nested
    class FindExistingLineTest {

        @Test
        void id에_대응되는_노선이_존재하는_경우_도메인으로_반환() {
            databaseFixtureUtils.saveLine("노선1", "색상", 1000);
            Line actual = repository.findExistingLine(1L);
            Line expected = new Line(1L, "노선1", "색상", 1000);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void id에_대응되는_노선이_존재하지_않는_경우_예외_발생() {
            assertThatThrownBy(() -> repository.findExistingLine(1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @DisplayName("checkExistingLine 메서드는 해당 id에 대응되는 노선의 존재 여부를 반환")
    @Nested
    class CheckExistingLineTest {

        @Test
        void 존재하는_노선의_id인_경우_참_반환() {
            databaseFixtureUtils.saveLine("존재", "색상");
            boolean actual = repository.checkExistingLine(1L);

            assertThat(actual).isTrue();
        }

        @Test
        void 존재하지_않는_노선의_id인_경우_거짓_반환() {
            boolean actual = repository.checkExistingLine(1L);

            assertThat(actual).isFalse();
        }
    }

    @DisplayName("checkExistingLineName 메서드는 해당 이름의 노선의 존재 여부를 반환")
    @Nested
    class CheckExistingLineNameTest {

        @Test
        void 존재하는_노선의_이름인_경우_참_반환() {
            databaseFixtureUtils.saveLine("이름!", "색상");
            boolean actual = repository.checkExistingLineName("이름!");

            assertThat(actual).isTrue();
        }

        @Test
        void 존재하지_않는_노선의_이름인_경우_거짓_반환() {
            boolean actual = repository.checkExistingLineName("없는 이름");

            assertThat(actual).isFalse();
        }
    }

    @DisplayName("saveLine 메서드는 새로운 노선과 구간을 저장하여 반환")
    @Nested
    class SaveLineTest {

        @Test
        void 생성된_노선의_도메인을_반환() {
            Line line = new Line("노선", "색상", 1000);
            Section initialSection = new Section(1L, STATION1, STATION2, 10);

            LineMap actual = repository.saveLine(line, initialSection);
            LineMap expected = LineMap.of(new Line(1L, "노선", "색상", 1000), initialSection);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 새로운_노선과_구간을_저장() {
            Line line = new Line("노선", "색상", 300);
            Section initialSection = new Section(1L, STATION1, STATION2, 10);
            repository.saveLine(line, initialSection);

            Line actualLine = lineDao.findById(1L).get();
            List<Section> actualSections = sectionDao.findAll();
            Line expectedLine = new Line(1L, "노선", "색상", 300);
            List<Section> expectedSections = List.of(
                    new Section(1L, STATION1, STATION2, 10));

            assertThat(actualLine).isEqualTo(expectedLine);
            assertThat(actualSections).isEqualTo(expectedSections);
        }
    }

    @Test
    void updateLine_메서드는_노선_정보를_수정() {
        databaseFixtureUtils.saveLine("기존 노선명", "색상", 200);

        repository.updateLine(new Line(1L, "새로운 노선명", "새로운 색상", 0));
        Line actual = lineDao.findById(1L).get();
        Line expected = new Line(1L, "새로운 노선명", "새로운 색상", 0);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteLine_메서드는_노선과_등록된_구간들을_제거() {
        databaseFixtureUtils.saveLine("노선1", "색상", 100);
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);

        repository.deleteLine(new Line(1L, "노선1", "색상", 100));
        boolean lineExistence = lineDao.findById(1L).isPresent();
        List<Section> existingSections = sectionDao.findAll();

        assertThat(lineExistence).isFalse();
        assertThat(existingSections).isEmpty();
    }
}
