package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.fare.Fare;
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

    private Long savedLineId;
    private Station savedStation1;
    private Station savedStation2;
    private Station savedStation3;
    private Station savedStation4;
    private Station savedStation5;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);

        LineDao lineDao = new LineDao(jdbcTemplate);
        StationDao stationDao = new StationDao(jdbcTemplate);

        Station newStation1 = new Station("선릉역");
        savedStation1 = stationDao.save(newStation1);

        Station newStation2 = new Station("삼성역");
        savedStation2 = stationDao.save(newStation2);

        Station newStation3 = new Station("종합운동장역");
        savedStation3 = stationDao.save(newStation3);

        Station newStation4 = new Station("잠실새내역");
        savedStation4 = stationDao.save(newStation4);

        Station newStation5 = new Station("잠실역");
        savedStation5 = stationDao.save(newStation5);

        Line newLine = new Line("2호선", "bg-red-600", new Fare(500),
                new Section(newStation1, newStation2, new Distance(10)));
        savedLineId = lineDao.save(newLine).getId();
    }

    @DisplayName("호선 ID를 통해 구간 목록을 가져온다.")
    @Test
    void findSectionsByLineId() {
        // given
        Long lineId = savedLineId;
        Section savedSection1 = new Section(savedStation1, savedStation2, new Distance(10));
        Section savedSection2 = new Section(savedStation2, savedStation3, new Distance(10));

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
        Long lineId = savedLineId;
        Section section1 = new Section(savedStation1, savedStation2, new Distance(10));
        Section section2 = new Section(savedStation2, savedStation3, new Distance(10));
        Section section3 = new Section(savedStation3, savedStation4, new Distance(10));
        Section section4 = new Section(savedStation4, savedStation5, new Distance(10));
        Sections sections = new Sections(List.of(section1, section2, section3, section4));

        // when
        saveSections(lineId, sections);
        List<Section> actual = sectionDao.findSectionsByLineId(lineId).getValue();
        List<Section> expected = sections.getValue();

        // then
        assertThat(actual).hasSameSizeAs(expected);
    }

    private void saveSections(Long lineId, Sections sections) {
        sectionDao.removeAllSectionsByLineId(lineId);

        for (Section section : sections.getValue()) {
            sectionDao.save(lineId, section);
        }
    }
}
