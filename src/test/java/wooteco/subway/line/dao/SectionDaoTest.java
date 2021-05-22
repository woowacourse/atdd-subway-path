package wooteco.subway.line.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("라인 Dao 기능 테스트")
@JdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SectionDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    Station 흑기역;
    Station 백기역;
    Station 낙성대역;
    Station 검프역;
    Line 이호선;
    Section headSection;
    Section tailSection;

    SectionDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionDao = new SectionDao(jdbcTemplate, dataSource);
        this.stationDao = new StationDao(jdbcTemplate, dataSource);
        this.lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @BeforeEach
    void init() {
        흑기역 = stationDao.insert(new Station("훅가역"));
        백기역 = stationDao.insert(new Station("백기역"));
        낙성대역 = stationDao.insert(new Station("낙성대역"));
        검프역 = stationDao.insert(new Station("검프역"));

        이호선 = lineDao.insert(new Line("이호선", "white"));

        headSection = sectionDao.insert(이호선, new Section(흑기역, 백기역, 4));
        tailSection = sectionDao.insert(이호선, new Section(백기역, 낙성대역, 8));

        이호선.addSection(headSection);
        이호선.addSection(tailSection);
    }

    @Test
    void insert() {
        //given
        //when
        //then
        assertThatCode(() -> sectionDao.insert(이호선, new Section(낙성대역, 검프역, 7)))
                .doesNotThrowAnyException();
    }

    @Test
    void deleteByLineId() {
        //given
        //when
        //then
        assertThatCode(() -> sectionDao.deleteByLineId(이호선.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    void insertSections() {
        //given
        Section section = sectionDao.insert(이호선, new Section(낙성대역, 검프역, 7));
        이호선.addSection(section);

        //when
        sectionDao.deleteByLineId(이호선.getId());
        sectionDao.insertSections(이호선);
        List<Section> sections = sectionDao.findAll();

        //then
        구간_목록_정상_포함됨(sections, 이호선.getSections().getSections());
    }

    @Test
    void findAll() {
        //given
        //when
        List<Section> sections = sectionDao.findAll();

        //then
        구간_목록_정상_포함됨(sections, Arrays.asList(headSection, tailSection));
    }

    private void 구간_목록_정상_포함됨(List<Section> sections, List<Section> originSections) {
        assertThat(toStations(sections)).containsExactlyInAnyOrderElementsOf(toStations(originSections));
    }

    private List<Station> toStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(
                        section.getUpStation(),
                        section.getDownStation()
                ))
                .distinct()
                .collect(Collectors.toList());
    }
}