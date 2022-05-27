package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.ui.dto.SectionRequest;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final Long lineId = 1L;
    private Station station1;
    private Station station2;
    private Long sectionId;

    private SectionDao sectionDao;


    @BeforeEach
    void init() {
        sectionDao = new SectionDao(jdbcTemplate);

        StationDao stationDao = new StationDao(jdbcTemplate);
        station1 = saveNewStation(stationDao, "강남역");
        station2 = saveNewStation(stationDao, "왕십리역");

        sectionId = sectionDao.save(new Section(lineId, station1, station2, 10));
    }

    private Station saveNewStation(StationDao stationDao, String name) {
        Long id = stationDao.save(new Station(name));
        return new Station(id, name);
    }

    @DisplayName("구간 전체 조회")
    @Test
    void findAll() {
        // given

        // when
        List<Section> sections = sectionDao.findAll();

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("구간을 노선 id로 검색")
    @Test
    void findByLineId() {
        // given

        // when
        List<Section> sections = sectionDao.findByLineId(lineId);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("구간 역 id로 조회")
    @Test
    void findByStationId() {
        // given

        // when
        List<Section> sections = sectionDao.findByStationId(station1.getId());

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("구간 역 id와 노선 id로 조회")
    @Test
    void findByLineIdAndStationId() {
        // given

        // when
        List<Section> sections = sectionDao.findByLineIdAndStationId(lineId, station2.getId());

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("구간 생성")
    @Test
    void save() {
        // given
        SectionRequest section = new SectionRequest(station1.getId(), station2.getId(), 10);

        // when
        Long id = sectionDao.save(section.toEntity(lineId, station1, station2));

        // then
        assertThat(id).isEqualTo(sectionId + 1);
    }

    @DisplayName("구간 수정")
    @Test
    void update() {
        // given
        Section updateSection = new Section(sectionId, lineId, station2, station1, 30);

        // when
        sectionDao.update(updateSection);

        // then
        Section section = sectionDao.findByLineId(lineId)
                .stream()
                .filter(it -> sectionId.equals(it.getId()))
                .findAny()
                .orElseThrow();

        assertThat(updateSection).isEqualTo(section);
    }

    @DisplayName("구간 노선 id로 삭제")
    @Test
    void deleteByLineId() {
        // given

        // when
        sectionDao.deleteByLineId(lineId);

        // then
        assertThat(sectionDao.findByLineId(lineId).size()).isEqualTo(0);
    }

    @DisplayName("구간 id로 삭제")
    @Test
    void deleteById() {
        // given

        // when
        sectionDao.deleteById(lineId);

        // then
        List<Section> sameSectionIds = sectionDao.findAll()
                .stream()
                .filter(section -> lineId.equals(section.getId()))
                .collect(Collectors.toList());

        assertThat(sameSectionIds.size()).isEqualTo(0);
    }
}