package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.dao.*;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class PathServiceTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private PathService pathService;

    @BeforeEach
    void setup() {
        stationDao = new StationDaoImpl(jdbcTemplate);
        sectionDao = new SectionDaoImpl(jdbcTemplate);
        lineDao = new LineDaoImpl(jdbcTemplate);
        pathService = new PathService(sectionDao, stationDao);
    }

    @Test
    void findShortestPath() {
        stationDao.save(new Station("강남역"));
        stationDao.save(new Station("교대역"));
        stationDao.save(new Station("역삼역"));
        stationDao.save(new Station("선릉역"));

        lineDao.save(new Line("1호선", "red"));

        sectionDao.save(new Section(1L, 1L, 2L, 3));
        sectionDao.save(new Section(1L, 2L, 3L, 4));
        sectionDao.save(new Section(1L, 3L, 4L, 5));

        PathServiceResponse pathServiceResponse = pathService.findShortestPath(new PathServiceRequest(1L, 4L, 20));

        assertThat(pathServiceResponse.getDistance()).isEqualTo(12);
    }
}
