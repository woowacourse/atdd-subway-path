package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.line.JdbcLineDao;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.dao.section.JdbcSectionDao;
import wooteco.subway.dao.section.SectionDao;
import wooteco.subway.dao.station.JdbcStationDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@JdbcTest
@Sql({"classpath:schema-truncate.sql", "classpath:init.sql"})
class JdbcSectionDaoTest {

    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        sectionDao = new JdbcSectionDao(jdbcTemplate);
        stationDao = new JdbcStationDao(jdbcTemplate);
        lineDao = new JdbcLineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Section 을 저장할 수 있다.")
    void save() {
        // given
        Line line = new Line(1L, "신분당선", "bg-red-600", 900);
        lineDao.save(line);
        Station upStation = stationDao.findById(stationDao.save(new Station("오리")));
        Station downStation = stationDao.findById(stationDao.save(new Station("배카라")));
        Section section = new Section(null, line, upStation, downStation, 1);

        // when
        long savedSectionId = sectionDao.save(section);

        // then
        assertThat(savedSectionId).isNotNull();
    }

    @Test
    @DisplayName("Line Id에 해당하는 Section을 조회할 수 있다.")
    void findAllByLineId() {
        Line line = new Line(1L, "신분당선", "bg-red-600", 900);
        long id = lineDao.save(line);
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));

        sectionDao.save(new Section(line, station1, station2, 2));
        sectionDao.save(new Section(line, station2, station3, 2));

        assertThat(sectionDao.findAllByLineId(id)).hasSize(2);
    }

    @Test
    @DisplayName("모든 Section을 조회할 수 있다.")
    void findAll() {
        Line line = new Line(1L, "신분당선", "bg-red-600", 900);
        lineDao.save(line);
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));

        sectionDao.save(new Section(line, station1, station2, 2));
        sectionDao.save(new Section(line, station2, station3, 2));

        List<Section> all = sectionDao.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("Sections를 업데이트할 수 있다.")
    void updateSections() {
        Line line = new Line(1L, "신분당선", "bg-red-600", 900);
        lineDao.save(line);
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));

        long sectionId1 = sectionDao.save(new Section(line, station1, station2, 1));
        long sectionId2 = sectionDao.save(new Section(line, station2, station3, 1));

        List<Section> sections = List.of(
                new Section(sectionId1, line, station1, station3, 3),
                new Section(sectionId2, line, station2, station3, 1));

        assertThat(sectionDao.updateSections(sections)).isEqualTo(2);
    }

    @Test
    @DisplayName("Section을 삭제할 수 있다.")
    void delete() {
        Line line = new Line(1L, "신분당선", "bg-red-600", 900);
        lineDao.save(line);
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        long sectionId = sectionDao.save(new Section(line, station1, station2, 10));

        assertThat(sectionDao.delete(sectionId)).isEqualTo(1);
    }
}
