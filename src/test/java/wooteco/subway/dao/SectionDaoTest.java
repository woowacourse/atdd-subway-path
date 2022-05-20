package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.SectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SectionDaoTest {

    private Long lineId;
    private Long stationId1;
    private Long stationId2;
    private Long sectionId;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @BeforeEach
    void init() {
        lineId = 1L;

        stationId1 = stationDao.save(new Station("강남역"));
        stationId2 = stationDao.save(new Station("왕십리역"));

        sectionId = sectionDao.save(new Section(lineId, stationId1, stationId2, 10));
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
        List<Section> sections = sectionDao.findByStationId(stationId1);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("구간 역 id와 노선 id로 조회")
    @Test
    void findByLineIdAndStationId() {
        // given

        // when
        List<Section> sections = sectionDao.findByLineIdAndStationId(lineId, stationId2);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("구간 생성")
    @Test
    void save() {
        // given
        SectionRequest section = new SectionRequest(stationId1, stationId2, 10);

        // when
        Long id = sectionDao.save(section.toEntity(lineId));

        // then
        assertThat(id).isEqualTo(sectionId + 1);
    }

    @DisplayName("구간 수정")
    @Test
    void update() {
        // given
        Section updateSection = new Section(sectionId, lineId, stationId2, stationId1, 30);

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