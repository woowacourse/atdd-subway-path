package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class SectionJdbcDaoTest extends DaoImplTest {

    private SectionJdbcDao sectionJdbcDao;
    private StationJdbcDao stationJdbcDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        sectionJdbcDao = new SectionJdbcDao(jdbcTemplate);
        stationJdbcDao = new StationJdbcDao(jdbcTemplate);
    }

    @DisplayName("구간 정보를 저장한다.")
    @Test
    void save() {
        Station upStation = stationJdbcDao.findById(1L);
        Station downStation = stationJdbcDao.findById(2L);

        Section section = new Section(1L, upStation, downStation, 12);
        Section newSection = sectionJdbcDao.save(section);

        assertThat(newSection.getLineId()).isEqualTo(1L);
        assertThat(newSection.getUpStation()).isEqualTo(upStation);
        assertThat(newSection.getDownStation()).isEqualTo(downStation);
        assertThat(newSection.getDistance()).isEqualTo(12);
    }

    @DisplayName("구간 정보들을 업데이트 한다.")
    @Test
    void update() {
        Station upStation = stationJdbcDao.findById(1L);
        Station downStation = stationJdbcDao.findById(2L);
        Section section = new Section(1L, upStation, downStation, 12);

        Section savedSection = sectionJdbcDao.save(section);

        Station newDownStation = new Station(3L, "이수역");
        Section newSection = new Section(savedSection.getId(), 1L, upStation, newDownStation, 7);
        List<Section> sections = List.of(newSection);

        assertThat(sectionJdbcDao.update(sections)).isEqualTo(1);
    }

    @DisplayName("역 정보를 통해 구간 정보를 삭제 한다.")
    @Test
    void deleteByStationId() {
        Station firstStation = stationJdbcDao.findById(1L);
        Station secondStation = stationJdbcDao.findById(2L);
        Station thirdStation = stationJdbcDao.findById(3L);

        Section section = new Section(1L, firstStation, secondStation, 12);
        Section nextSection = new Section(2L, secondStation, thirdStation, 4);

        sectionJdbcDao.save(section);
        Section newSection = sectionJdbcDao.save(nextSection);

        assertThat(sectionJdbcDao.delete(newSection)).isEqualTo(1);
    }
}
