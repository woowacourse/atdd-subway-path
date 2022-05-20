package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.LineCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class StationDaoTest {

    private Long stationId1;
    private Long stationId2;
    private Long lineId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @BeforeEach
    void init() {
        truncate();

        stationId1 = stationDao.save(new Station("강남역"));
        stationId2 = stationDao.save(new Station("왕십리역"));

        LineCreateRequest request = new LineCreateRequest("신분당선", "red", stationId1, stationId2, 10, 500);
        lineId = lineDao.save(request);

        sectionDao.save(new Section(lineId, stationId1, stationId2, 10));
    }

    void truncate() {
        jdbcTemplate.update("truncate table STATION");
        jdbcTemplate.update("truncate table SECTION");
        jdbcTemplate.update("truncate table LINE");
    }

    @DisplayName("역 저장")
    @Test
    void save() {
        // given
        Station station = new Station("정자역");

        // when
        Long id = stationDao.save(station);

        // then
        assertThat(id).isEqualTo(stationId2 + 1);
    }

    @DisplayName("역 이름 존재하는지 확인")
    @Test
    void countByName() {
        // given

        // when
        boolean result = stationDao.existsByName("강남역");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("역 id 존재하는지 확인")
    @Test
    void countById() {
        // given

        // when
        boolean result = stationDao.existsById(stationId1);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("역 id로 검색")
    @Test
    void findById() {
        // given

        // when
        Station station = stationDao.findById(stationId1);

        // then
        assertThat(station.getId()).isEqualTo(stationId1);
    }

    @DisplayName("역 전체 조회")
    @Test
    void findAll() {
        // given

        // when
        List<Station> stations = stationDao.findAll();

        // then
        assertThat(stations.size()).isEqualTo(2);
    }

    @DisplayName("역 삭제")
    @Test
    void deleteById() {
        // given
        List<Station> before = stationDao.findAll();

        // when
        stationDao.deleteById(stationId1);
        List<Station> after = stationDao.findAll();

        // then
        assertThat(before.size() - 1).isEqualTo(after.size());
    }

    @DisplayName("노선 ID로 조회")
    @Test
    void findByLineId() {
        // given

        // when
        List<Station> stations = stationDao.findByLineId(lineId);

        // then
        assertThat(stations.size()).isEqualTo(2);
    }
}