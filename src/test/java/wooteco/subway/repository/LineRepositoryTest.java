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
import wooteco.subway.domain.line.LineExtraFare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.entity.LineEntity;
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

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 잠실역 = new Station(2L, "잠실역");
    private static final Station 선릉역 = new Station(3L, "선릉역");

    private static final LineEntity 신분당선 = new LineEntity(1L, "신분당선", "색깔1", 1000);
    private static final LineEntity 분당선 = new LineEntity(2L, "분당선", "색깔2", 0);
    private static final LineEntity 수인선 = new LineEntity(3L, "수인선", "색깔3", 900);

    @BeforeEach
    void setup() {
        databaseFixtureUtils.saveStations(강남역, 잠실역, 선릉역);
    }

    @Test
    void findAllLines_메서드는_모든_노선_정보들을_조회하여_도메인들의_리스트로_반환() {
        Section 강남_선릉 = new Section(1L, 강남역, 선릉역, 20);
        Section 분당선_강남_잠실 = new Section(2L, 강남역, 잠실역, 10);
        Section 수인선_강남_잠실 = new Section(3L, 강남역, 잠실역, 10);
        Section 잠실_선릉 = new Section(3L, 잠실역, 선릉역, 10);
        databaseFixtureUtils.saveLines(신분당선, 분당선, 수인선);
        databaseFixtureUtils.saveSections(강남_선릉, 분당선_강남_잠실, 수인선_강남_잠실, 잠실_선릉);

        List<Line> actual = repository.findAllLines();
        List<Line> expected = List.of(generateLine(신분당선, 강남_선릉),
                generateLine(분당선, 분당선_강남_잠실), generateLine(수인선, 수인선_강남_잠실, 잠실_선릉));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findLineExtraFaresByIds_메서드는_id_목록에_해당되는_노선들의_추가요금_정보들을_조회하여_리스트로_반환() {
        LineEntity 신분당선 = new LineEntity("신분당선", "색깔1", 1000);
        LineEntity 분당선 = new LineEntity("분당선", "색깔2", 0);
        LineEntity 수인선 = new LineEntity("수인선", "색깔3", 900);
        databaseFixtureUtils.saveLines(신분당선, 분당선, 수인선);

        List<LineExtraFare> actual = repository.findLineExtraFaresByIds(List.of(2L, 3L));
        List<LineExtraFare> expected = List.of(new LineExtraFare(0), new LineExtraFare(900));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("findExistingLine 메서드는 id에 대응되는 노선을 조회")
    @Nested
    class FindExistingLineTest {

        @Test
        void id에_대응되는_노선이_존재하는_경우_도메인으로_반환() {
            Section 강남_선릉 = new Section(1L, 강남역, 선릉역, 10);
            saveLineTestFixture("노선1", "색상", 1000);
            databaseFixtureUtils.saveSections(강남_선릉);

            Line actual = repository.findExistingLine(1L);
            Line expected = generateLine(1L, "노선1", "색상", 1000, 강남_선릉);

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
            databaseFixtureUtils.saveLines(신분당선);
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
            databaseFixtureUtils.saveLines(new LineEntity("이름!", "색상", 0));
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
            Section initialSection = new Section(1L, 강남역, 잠실역, 10);
            Line line = new Line("노선", "색상", 1000, initialSection);

            Line actual = repository.saveLine(line);
            Line expected = generateLine(1L, "노선", "색상", 1000, initialSection);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 새로운_노선과_구간을_저장() {
            Section initialSection = new Section(1L, 강남역, 잠실역, 10);
            Line line = new Line("노선", "색상", 300, initialSection);
            repository.saveLine(line);

            LineEntity actualLine = lineDao.findById(1L).get();
            List<Section> actualSections = sectionDao.findAll();
            LineEntity expectedLine = new LineEntity(1L, "노선", "색상", 300);
            List<Section> expectedSections = List.of(new Section(1L, 강남역, 잠실역, 10));

            assertThat(actualLine).isEqualTo(expectedLine);
            assertThat(actualSections).isEqualTo(expectedSections);
        }
    }

    @Test
    void updateLine_메서드는_노선_정보를_수정() {
        Section 강남_선릉 = new Section(1L, 강남역, 선릉역, 10);
        saveLineTestFixture("기존 노선명", "색상", 200);
        databaseFixtureUtils.saveSections(강남_선릉);

        repository.updateLine(generateLine(1L, "새로운 노선명", "새로운 색상", 0, 강남_선릉));
        LineEntity actual = lineDao.findById(1L).get();
        LineEntity expected = new LineEntity(1L, "새로운 노선명", "새로운 색상", 0);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteLine_메서드는_노선과_등록된_구간들을_제거() {
        Section 강남_잠실 = new Section(1L, 강남역, 잠실역, 10);
        saveLineTestFixture("노선1", "색상", 100);
        databaseFixtureUtils.saveSections(강남_잠실);

        repository.deleteLine(generateLine(1L, "노선1", "색상", 100, 강남_잠실));
        boolean lineExistence = lineDao.findById(1L).isPresent();
        List<Section> existingSections = sectionDao.findAll();

        assertThat(lineExistence).isFalse();
        assertThat(existingSections).isEmpty();
    }

    private Line generateLine(LineEntity lineEntity, Section... sections) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare(),
                new Sections(sections));
    }

    private Line generateLine(Long id, String name, String color, int extraFare, Section... sections) {
        return new Line(id, name, color, extraFare, new Sections(sections));
    }

    private void saveLineTestFixture(String name, String color, int extraFare) {
        databaseFixtureUtils.saveLines(new LineEntity(name, color, extraFare));
    }
}
