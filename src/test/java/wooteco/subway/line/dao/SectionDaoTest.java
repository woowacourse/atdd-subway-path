package wooteco.subway.line.dao;

import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;

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
    }

    @Test
    void insert() {
    }

    @Test
    void deleteByLineId() {
    }

    @Test
    void insertSections() {
    }

    @Test
    void findAll() {
        //given
        //when
        List<Section> sections = sectionDao.findAll();

        //then
        구간_목록_정상_포함됨(sections, Arrays.asList(headSection, tailSection));
    }

    private void 구간_목록_정상_포함됨(List<Section> sections, List<Section> originSection) {
        assertThat(sections.containsAll(originSection)).isEqualTo(true);
    }
}