package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.service.ServiceTestFixture.deleteAllLine;
import static wooteco.subway.service.ServiceTestFixture.deleteAllStation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.JdbcLineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.JdbcSectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.JdbcStationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathResponse;

@JdbcTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;
    private PathService pathService;

    @BeforeEach
    void setUp() {
        sectionDao = new JdbcSectionDao(jdbcTemplate);
        stationDao = new JdbcStationDao(jdbcTemplate);
        pathService = new PathService(sectionDao, stationDao);
        lineDao = new JdbcLineDao(jdbcTemplate);

        deleteAllLine(lineDao);
        deleteAllStation(stationDao);
    }

    @Test
    void findShortestPath() {
        // given
        Long savedId1 = stationDao.save(new Station("교대역"));
        Long savedId2 = stationDao.save(new Station("강남역"));
        Long savedId3 = stationDao.save(new Station("역삼역"));

        Line line = new Line("2호선", "green");
        Long savedLineId = lineDao.save(line);

        Section section1 = new Section(savedLineId, savedId1, savedId2, 3);
        Section section2 = new Section(savedLineId, savedId2, savedId3, 4);

        sectionDao.save(section1);
        sectionDao.save(section2);
        PathServiceRequest pathServiceRequest = new PathServiceRequest(savedId1, savedId3, 20);

        // when
        PathResponse result = pathService.findShortestPath(pathServiceRequest);

        // then
        assertThat(result.getStations().size()).isEqualTo(3);
        assertThat(result.getFare()).isEqualTo(1250);
        assertThat(result.getDistance()).isEqualTo(7);
    }
}
