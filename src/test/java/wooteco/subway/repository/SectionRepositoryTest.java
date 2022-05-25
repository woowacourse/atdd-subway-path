package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class SectionRepositoryTest extends DatabaseUsageTest {

    @Autowired
    private SectionRepository repository;

    @Autowired
    private SectionDao sectionDao;

    private static final Station STATION1 = new Station(1L, "강남역");
    private static final Station STATION2 = new Station(2L, "잠실역");
    private static final Station STATION3 = new Station(3L, "선릉역");
    private static final Station STATION4 = new Station(4L, "청계산입구역");

    @BeforeEach
    void setup() {
        databaseFixtureUtils.saveStations("강남역", "잠실역", "선릉역", "청계산입구역");
    }

    @Test
    void findAllSections_메서드는_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);
        databaseFixtureUtils.saveSection(2L, 1L, 3L, 5);

        List<Section> actual = repository.findAllSections();
        List<Section> expected = List.of(
                new Section(1L, STATION1, STATION2, 10),
                new Section(1L, STATION2, STATION3, 15),
                new Section(2L, STATION1, STATION3, 5));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllSectionsByLineId_메서드는_특정_노선에_등록된_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);
        databaseFixtureUtils.saveSection(2L, 1L, 3L, 5);

        List<Section> actual = repository.findAllSectionsByLineId(1L);
        List<Section> expected = List.of(
                new Section(1L, STATION1, STATION2, 10),
                new Section(1L, STATION2, STATION3, 15));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllSectionsByStationId_메서드는_특정_지하철역이_등록된_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);
        databaseFixtureUtils.saveSection(2L, 1L, 3L, 5);

        List<Section> actual = repository.findAllSectionsByStationId(1L);
        List<Section> expected = List.of(
                new Section(1L, STATION1, STATION2, 10),
                new Section(2L, STATION1, STATION3, 5));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveSections_메서드는_구간들을_저장() {
        List<Section> sections = List.of(
                new Section(STATION1, STATION2, 10),
                new Section(STATION2, STATION3, 5));
        repository.saveSections(1L, sections);

        List<Section> actual = sectionDao.findAll();
        List<Section> expected = List.of(
                new Section(1L, STATION1, STATION2, 10),
                new Section(1L, STATION2, STATION3, 5));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteSections_메서드는_구간들을_제거() {
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);
        databaseFixtureUtils.saveSection(1L, 3L, 4L, 5);
        databaseFixtureUtils.saveSection(2L, 1L, 3L, 5);
        List<Section> sections = List.of(new Section(STATION1, STATION2, 10),
                new Section(STATION2, STATION3, 15));

        repository.deleteSections(1L, sections);
        List<Section> actual = sectionDao.findAll();
        List<Section> expected = List.of(
                new Section(1L, STATION3, STATION4, 5),
                new Section(2L, STATION1, STATION3, 5));

        assertThat(actual).isEqualTo(expected);
    }
}
