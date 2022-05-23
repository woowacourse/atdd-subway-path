package wooteco.subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.*;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private PathService pathService;

    @BeforeEach
    void setUp(){
        stationDao = new StationJdbcDao(jdbcTemplate);
        sectionDao = new SectionJdbcDao(jdbcTemplate);
        lineDao = new LineJdbcDao(jdbcTemplate);
        pathService = new PathService(sectionDao, stationDao, lineDao);
    }

    @DisplayName("환승을 하면 거치는 라인 중 비싼 금액이 반영되어 요금을 결정한다.")
    @Test
    void findPath() {
        Line line1 = lineDao.save(new Line("2호선", "green", 500));
        Line line2 = lineDao.save(new Line("3호선", "orange", 600));
        Station station1 = stationDao.save(new Station("교대역"));
        Station station2 = stationDao.save(new Station("남부터미널역"));
        Station station3 = stationDao.save(new Station("강남역"));
        Station station5 = stationDao.save(new Station("선릉역"));

        Section section1To2 = sectionDao.save(line1.getId(), new Section(line1.getId(), station1, station2, 10));
        Section section2To3 = sectionDao.save(line1.getId(), new Section(line1.getId(), station2, station3, 10));

        Section section3To5 = sectionDao.save(line2.getId(), new Section(line2.getId(), station3, station5, 10));
        PathResponse path = pathService.findPath(station1.getId(), station5.getId(), 50);
        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(30),
                () -> assertThat(path.getStations())
                        .extracting(StationResponse::getName)
                        .containsExactly(station1.getName(), station2.getName(), station3.getName(), station5.getName()),
                () -> assertThat(path.getFare()).isEqualTo(2250)
        );

    }
}
