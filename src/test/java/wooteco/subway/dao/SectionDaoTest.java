package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.ConcreteCreationStrategy;
import wooteco.subway.domain.section.ConcreteDeletionStrategy;
import wooteco.subway.domain.section.ConcreteSortStrategy;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;

@JdbcTest
@Sql("classpath:truncate.sql")
public class SectionDaoTest {

    @Autowired
    private DataSource dataSource;

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    private Line line;

    private Long 강남 = 1L;
    private Long 선릉 = 2L;
    private Long 잠실 = 3L;
    private Long 사당 = 4L;
    private Long 지하철_2호선_아이디 = 1L;

    private Station 강남역 = new Station(강남, "강남역");
    private Station 선릉역 = new Station(선릉, "선릉역");
    private Station 잠실역 = new Station(잠실, "잠실역");

    private Line 지하철_2호선 = new Line("지하철2호선", "green", 300);

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(dataSource);
        lineDao = new LineDao(dataSource);
        stationDao = new StationDao(dataSource);

        stationDao.insert(강남역);
        stationDao.insert(선릉역);
        stationDao.insert(잠실역);

        line = lineDao.insert(지하철_2호선);

        sectionDao.insert(new Section( 지하철_2호선_아이디, 강남, 선릉, 10));
    }

    @DisplayName("새로운 구간을 추가한다.")
    @Test
    void insert() {
        Line newLine = lineDao.insert(new Line("신분당선", "orange", 300));

        assertThatCode(() ->
                sectionDao.insert(new Section(newLine.getId(), 강남, 선릉, 5)))
                .doesNotThrowAnyException();
    }

    @DisplayName("구간을 변경한다.")
    @Test
    void update() {
        sectionDao.update(new Section(1L, 지하철_2호선_아이디, 잠실, 선릉, 5));

        Sections sections = new Sections(sectionDao.getByLineId(지하철_2호선_아이디), new ConcreteCreationStrategy(),
                new ConcreteDeletionStrategy(), new ConcreteSortStrategy());

        Section newSection = sections.getSections().get(0);

        assertThat(newSection.getUpStationId()).isEqualTo(잠실);
        assertThat(newSection.getDownStationId()).isEqualTo(선릉);
    }

    @DisplayName("lineId 값에 해당하는 모든 구간의 정보를 가져온다.")
    @Test
    void findByLineId() {
        sectionDao.insert(new Section(지하철_2호선_아이디, 선릉, 잠실, 5));
        Sections sections = new Sections(sectionDao.getByLineId(지하철_2호선_아이디), new ConcreteCreationStrategy(),
                new ConcreteDeletionStrategy(), new ConcreteSortStrategy());

        assertThat(sections.getSections()).hasSize(2);
    }

    @DisplayName("lineId와 stationId에 해당하는 구간을 삭제한다.")
    @Test
    void deleteByLineIdAndStationId() {
        sectionDao.insert(new Section(지하철_2호선_아이디, 선릉, 잠실, 5));

        sectionDao.deleteByLineIdAndStationId(지하철_2호선_아이디, 선릉);
        List<Section> sections = sectionDao.getByLineId(지하철_2호선_아이디);

        assertThat(sections).hasSize(0);
    }

    @DisplayName("노선에 해당하는 모든 구간을 조회한다.")
    @Test
    void getByLineId() {
        sectionDao.insert(new Section(지하철_2호선_아이디, 잠실, 선릉, 5));

        List<Section> sections = sectionDao.getByLineId(지하철_2호선_아이디);

        assertThat(sections).hasSize(2);
    }

    @DisplayName("존재하는 모든 구간을 조회한다.")
    @Test
    void findAll() {
        sectionDao.insert(new Section(지하철_2호선_아이디, 선릉, 잠실, 5));

        List<Section> sections = sectionDao.findAll();

        assertThat(sections).hasSize(2);
    }
}
