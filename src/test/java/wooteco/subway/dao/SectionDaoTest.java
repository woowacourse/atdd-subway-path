package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.삼성역;
import static wooteco.subway.Fixtures.선릉역;
import static wooteco.subway.Fixtures.이호선;
import static wooteco.subway.Fixtures.잠실새내역;
import static wooteco.subway.Fixtures.잠실역;
import static wooteco.subway.Fixtures.종합운동장역;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    private Station 저장된_선릉역;
    private Station 저장된_삼성역;
    private Station 저장된_종합운동장역;
    private Station 저장된_잠실새내역;
    private Station 저장된_잠실역;

    private Line 저장된_이호선;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        LineDao lineDao = new LineDao(jdbcTemplate);
        StationDao stationDao = new StationDao(jdbcTemplate);

        저장된_선릉역 = stationDao.save(선릉역);
        저장된_삼성역 = stationDao.save(삼성역);
        저장된_종합운동장역 = stationDao.save(종합운동장역);
        저장된_잠실새내역 = stationDao.save(잠실새내역);
        저장된_잠실역 = stationDao.save(잠실역);

        저장된_이호선 = lineDao.save(이호선);
    }

    @DisplayName("호선 ID를 통해 구간 목록을 가져온다.")
    @Test
    void findSectionsByLineId() {
        // given
        Long lineId = 저장된_이호선.getId();
        Section savedSection1 = new Section(저장된_선릉역, 저장된_삼성역, new Distance(10));
        Section savedSection2 = new Section(저장된_삼성역, 저장된_종합운동장역, new Distance(10));

        Sections sections = new Sections(List.of(savedSection1, savedSection2));
        saveSections(lineId, sections);

        // when
        Sections foundSections = sectionDao.findSectionsByLineId(lineId);
        List<Section> actual = foundSections.getValue();
        List<Station> actualUpStations = actual.stream().map(Section::getUpStation).collect(Collectors.toList());
        List<Station> actualDownStations = actual.stream().map(Section::getDownStation).collect(Collectors.toList());
        List<Distance> actualDistances = actual.stream().map(Section::getDistance).collect(Collectors.toList());

        List<Section> expected = sections.getValue();
        List<Station> expectedUpStations = expected.stream().map(Section::getUpStation).collect(Collectors.toList());
        List<Station> expectedDownStations = expected.stream().map(Section::getDownStation)
                .collect(Collectors.toList());
        List<Distance> expectedDistances = expected.stream().map(Section::getDistance).collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(actualUpStations).containsAll(expectedUpStations),
                () -> assertThat(actualDownStations).containsAll(expectedDownStations),
                () -> assertThat(actualDistances).containsAll(expectedDistances)
        );
    }

    @DisplayName("호선 ID와 구간 목록을 전달받아 구간 목록을 저장한다.")
    @Test
    void saveSections() {
        // given
        Long lineId = 저장된_이호선.getId();
        Section section1 = new Section(저장된_선릉역, 저장된_삼성역, new Distance(10));
        Section section2 = new Section(저장된_삼성역, 저장된_종합운동장역, new Distance(10));
        Section section3 = new Section(저장된_종합운동장역, 저장된_잠실새내역, new Distance(10));
        Section section4 = new Section(저장된_잠실새내역, 저장된_잠실역, new Distance(10));
        Sections sections = new Sections(List.of(section1, section2, section3, section4));

        // when
        saveSections(lineId, sections);
        List<Section> actual = sectionDao.findSectionsByLineId(lineId).getValue();
        List<Section> expected = sections.getValue();

        // then
        assertThat(actual).hasSameSizeAs(expected);
    }

    private void saveSections(Long lineId, Sections sections) {
        sectionDao.deleteAllSectionsByLineId(lineId);

        for (Section section : sections.getValue()) {
            sectionDao.save(lineId, section);
        }
    }
}
