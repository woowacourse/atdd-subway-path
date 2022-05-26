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

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 잠실역 = new Station(2L, "잠실역");
    private static final Station 선릉역 = new Station(3L, "선릉역");
    private static final Station 청계산입구역 = new Station(4L, "청계산입구역");

    @BeforeEach
    void setup() {
        databaseFixtureUtils.saveStations(강남역, 잠실역, 선릉역, 청계산입구역);
    }

    @Test
    void findAllSections_메서드는_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        Section 강남_잠실 = new Section(1L, 강남역, 잠실역, 10);
        Section 잠실_선릉 = new Section(1L, 잠실역, 선릉역, 15);
        Section 강남_선릉 = new Section(2L, 강남역, 선릉역, 5);
        databaseFixtureUtils.saveSections(강남_잠실, 잠실_선릉, 강남_선릉);

        List<Section> actual = repository.findAllSections();
        List<Section> expected = List.of(강남_잠실, 잠실_선릉, 강남_선릉);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllSectionsByLineId_메서드는_특정_노선에_등록된_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        Section 강남_잠실 = new Section(1L, 강남역, 잠실역, 10);
        Section 잠실_선릉 = new Section(1L, 잠실역, 선릉역, 15);
        Section 강남_선릉 = new Section(2L, 강남역, 선릉역, 5);
        databaseFixtureUtils.saveSections(강남_잠실, 잠실_선릉, 강남_선릉);

        List<Section> actual = repository.findAllSectionsByLineId(1L);
        List<Section> expected = List.of(강남_잠실, 잠실_선릉);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllSectionsByStationId_메서드는_특정_지하철역이_등록된_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        Section 강남_잠실 = new Section(1L, 강남역, 잠실역, 10);
        Section 잠실_선릉 = new Section(1L, 잠실역, 선릉역, 15);
        Section 강남_선릉 = new Section(2L, 강남역, 선릉역, 5);
        databaseFixtureUtils.saveSections(강남_잠실, 잠실_선릉, 강남_선릉);

        List<Section> actual = repository.findAllSectionsByStationId(1L);
        List<Section> expected = List.of(강남_잠실, 강남_선릉);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveSections_메서드는_구간들을_저장() {
        List<Section> sections = List.of(
                new Section(강남역, 잠실역, 10),
                new Section(잠실역, 선릉역, 5));
        repository.saveSections(1L, sections);

        List<Section> actual = sectionDao.findAll();
        List<Section> expected = List.of(
                new Section(1L, 강남역, 잠실역, 10),
                new Section(1L, 잠실역, 선릉역, 5));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteSections_메서드는_구간들을_제거() {
        Section 강남_잠실 = new Section(1L, 강남역, 잠실역, 10);
        Section 잠실_선릉 = new Section(1L, 잠실역, 선릉역, 15);
        Section 선릉_청계산 = new Section(1L, 선릉역, 청계산입구역, 5);
        Section 강남_선릉 = new Section(2L, 강남역, 선릉역, 5);
        databaseFixtureUtils.saveSections(강남_잠실, 잠실_선릉, 선릉_청계산, 강남_선릉);
        List<Section> sections = List.of(new Section(강남역, 잠실역, 10),
                new Section(잠실역, 선릉역, 15));

        repository.deleteSections(1L, sections);
        List<Section> actual = sectionDao.findAll();
       List<Section> expected = List.of(선릉_청계산, 강남_선릉);

        assertThat(actual).isEqualTo(expected);
    }
}
