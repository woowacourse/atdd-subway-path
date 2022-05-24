package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.entity.StationEntity;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class SectionRepositoryTest extends DatabaseUsageTest {

    @Autowired
    private SectionRepository repository;

    @Autowired
    private SectionDao sectionDao;

    private final Station station1 = new Station(1L, "강남역");
    private final Station station2 = new Station(2L, "잠실역");
    private final Station station3 = new Station(3L, "선릉역");
    private final StationEntity stationEntity1 = new StationEntity(1L, "강남역");
    private final StationEntity stationEntity2 = new StationEntity(2L, "잠실역");
    private final StationEntity stationEntity3 = new StationEntity(3L, "선릉역");
    private final StationEntity stationEntity4 = new StationEntity(4L, "청계산입구역");

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
                new Section(1L, station1, station2, 10),
                new Section(1L, station2, station3, 15),
                new Section(2L, station1, station3, 5));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllSectionsByLineId_메서드는_특정_노선에_등록된_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);
        databaseFixtureUtils.saveSection(2L, 1L, 3L, 5);

        List<Section> actual = repository.findAllSectionsByLineId(1L);
        List<Section> expected = List.of(
                new Section(1L, station1, station2, 10),
                new Section(1L, station2, station3, 15));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllSectionsByStationId_메서드는_특정_지하철역이_등록된_모든_구간_정보들을_조회하여_도메인들의_리스트로_반환() {
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);
        databaseFixtureUtils.saveSection(2L, 1L, 3L, 5);

        List<Section> actual = repository.findAllSectionsByStationId(1L);
        List<Section> expected = List.of(
                new Section(1L, station1, station2, 10),
                new Section(2L, station1, station3, 5));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveSections_메서드는_구간들을_저장() {
        List<Section> sections = List.of(
                new Section(station1, station2, 10),
                new Section(station2, station3, 5));
        repository.saveSections(1L, sections);

        List<SectionEntity> actual = sectionDao.findAll();
        List<SectionEntity> expected = List.of(
                new SectionEntity(1L, stationEntity1, stationEntity2, 10),
                new SectionEntity(1L, stationEntity2, stationEntity3, 5));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteSections_메서드는_구간들을_제거() {
        databaseFixtureUtils.saveSection(1L, 1L, 2L, 10);
        databaseFixtureUtils.saveSection(1L, 2L, 3L, 15);
        databaseFixtureUtils.saveSection(1L, 3L, 4L, 5);
        databaseFixtureUtils.saveSection(2L, 1L, 3L, 5);
        List<Section> sections = List.of(new Section(station1, station2, 10),
                new Section(station2, station3, 15));

        repository.deleteSections(1L, sections);
        List<SectionEntity> actual = sectionDao.findAll();
        List<SectionEntity> expected = List.of(
                new SectionEntity(1L, stationEntity3, stationEntity4, 5),
                new SectionEntity(2L, stationEntity1, stationEntity3, 5));

        assertThat(actual).isEqualTo(expected);
    }
}
