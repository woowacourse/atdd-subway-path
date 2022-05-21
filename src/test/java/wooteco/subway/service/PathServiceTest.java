package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.dto.*;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private PathService pathService;

    private StationResponse 선릉역;
    private StationResponse 선정릉역;
    private StationResponse 한티역;
    private StationResponse 모란역;
    private StationResponse 기흥역;
    private StationResponse 강남역;

    @BeforeEach
    void setUp() {
        pathService = new PathService(
                new LineDao(jdbcTemplate, dataSource),
                new StationDao(jdbcTemplate, dataSource),
                new SectionDao(jdbcTemplate, dataSource));
    }

    void setUpSubwayMap() {
        SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        StationService stationService = new StationService(new StationDao(jdbcTemplate, dataSource));
        LineService lineService = new LineService(new LineDao(jdbcTemplate, dataSource),
                new StationDao(jdbcTemplate, dataSource), sectionDao);
        SectionService sectionService = new SectionService(new LineDao(jdbcTemplate, dataSource),
                new StationDao(jdbcTemplate, dataSource), sectionDao);

        선릉역 = stationService.create(new StationRequest("선릉역"));
        선정릉역 = stationService.create(new StationRequest("선정릉역"));
        한티역 = stationService.create(new StationRequest("한티역"));
        모란역 = stationService.create(new StationRequest("모란역"));
        기흥역 = stationService.create(new StationRequest("기흥역"));
        강남역 = stationService.create(new StationRequest("강남역"));

        LineResponse 분당선 = lineService.create(new LineRequest("분당선", "yellow", 선릉역.getId(),
                선정릉역.getId(), 50, 0));
        sectionDao.insert(new Section(분당선.getId(), 선정릉역.getId(), 한티역.getId(), 8));
        sectionDao.insert(new Section(분당선.getId(), 한티역.getId(), 강남역.getId(), 20));

        LineResponse 신분당선 = lineService.create(new LineRequest("신분당선", "red", 기흥역.getId(),
                모란역.getId(), 10, 500));
        sectionDao.insert(new Section(신분당선.getId(), 모란역.getId(), 강남역.getId(), 5));

        LineResponse 우테코선 = lineService.create(new LineRequest("우테코선", "red", 모란역.getId(),
                선정릉역.getId(), 6, 700));
    }

    @DisplayName("경로를 올바르게 조회한다.")
    @Test
    void findPath() {
        setUpSubwayMap();
        PathFindResponse result = pathService.findPath(기흥역.getId(), 한티역.getId(), 20);
        System.out.println(result.getStations());
        assertAll(
                () -> assertThat(result.getStations().get(0).getId()).isEqualTo(기흥역.getId()),
                () -> assertThat(result.getStations().get(1).getId()).isEqualTo(모란역.getId()),
                () -> assertThat(result.getStations().get(2).getId()).isEqualTo(선정릉역.getId()),
                () -> assertThat(result.getStations().get(3).getId()).isEqualTo(한티역.getId()),
                () -> assertThat(result.getDistance()).isEqualTo(24),
                () -> assertThat(result.getFare()).isEqualTo(1550)
        );
    }
}
