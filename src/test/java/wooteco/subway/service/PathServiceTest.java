package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.LineDaoImpl;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.SectionDaoImpl;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.StationDaoImpl;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;

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
        sectionDao = new SectionDaoImpl(jdbcTemplate);
        stationDao = new StationDaoImpl(jdbcTemplate);
        pathService = new PathService(sectionDao, stationDao);
        lineDao = new LineDaoImpl(jdbcTemplate);

        Station station1 = new Station("교대역");
        Station station2 = new Station("강남역");
        Station station3 = new Station("역삼역");

        Long savedId1 = stationDao.save(station1);
        Long savedId2 = stationDao.save(station2);
        Long savedId3 = stationDao.save(station3);

        Line line = new Line("2호선", "green");
        Long savedLineId = lineDao.save(line);

        Section section1 = new Section(savedLineId, savedId1, savedId2, 3);
        Section section2 = new Section(savedLineId, savedId2, savedId3, 4);

        sectionDao.save(section1);
        sectionDao.save(section2);
    }

    @Test
    void findShortestPath() {
        // given
        PathServiceRequest pathServiceRequest = new PathServiceRequest(1L, 3L, 20);

        // when
        PathServiceResponse result = pathService.findShortestPath(pathServiceRequest);

        // then
        assertThat(result.getStations().size()).isEqualTo(3);
        assertThat(result.getFare()).isEqualTo(1250);
        assertThat(result.getDistance()).isEqualTo(7);
    }
}
