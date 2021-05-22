package wooteco.subway.section.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.line.domain.Line;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class SectionDaoTest {

    private SectionDao sectionDao;
    private StationDao stationDao;
    private Station firstStation;
    private Station secondStation;
    private Station thirdStation;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(dataSource);
        jdbcTemplate.execute("create table if not exists STATION ( id bigint auto_increment not null, name varchar(255) not null unique, primary key(id))");
        jdbcTemplate.execute("create table if not exists SECTION ( id bigint auto_increment not null, line_id bigint not null, up_station_id bigint not null, " +
                "down_station_id bigint not null, distance int not null, primary key(id), foreign key (up_station_id) references station(id), foreign key (down_station_id) references station(id))");
        firstStation = stationDao.insert(new Station("잠원역"));
        secondStation = stationDao.insert(new Station("강남역"));
        thirdStation = stationDao.insert(new Station("역삼역"));
    }

    @DisplayName("모든 구간들을 조회한다.")
    @Test
    void findAll() {
        Line line = new Line(1L, "1호선", "black");
        Section firstSection = new Section(firstStation, secondStation, 5);
        Section secondSection = new Section(secondStation, thirdStation, 31);

        Section savedSection = sectionDao.insert(line, firstSection);
        Section savedSection2 = sectionDao.insert(line, secondSection);

        List<Section> sections = sectionDao.findAll();

        assertThat(sections).contains(savedSection, savedSection2);
    }
}
