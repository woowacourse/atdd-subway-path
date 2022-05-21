package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.dao.section.SectionDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.path.PathFindRequest;
import wooteco.subway.dto.path.PathFindResponse;

@SpringBootTest
@Sql({"classpath:schema-truncate.sql", "classpath:init.sql"})
class PathServiceTest {

    @Autowired
    SectionDao sectionDao;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    PathService pathService;

    @Test
    @DisplayName("경로를 조회할 수 있다.")
    void findPath() {
        // given
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));
        Station station4 = stationDao.findById(stationDao.save(new Station("레넌")));

        Line line1 = new Line(1L, "1호선", "bg-red-500", 0);
        Line line2 = new Line(2L, "2호선", "bg-green-500", 900);
        lineDao.save(line1);
        lineDao.save(line2);

        sectionDao.save(new Section(line1, station1, station2, 2));
        sectionDao.save(new Section(line1, station2, station3, 2));
        sectionDao.save(new Section(line2, station1, station4, 3));
        sectionDao.save(new Section(line2, station4, station3, 3));

        // when
        PathFindResponse path = pathService.findPath(new PathFindRequest(station1.getId(), station3.getId(), 15));

        // then
        assertAll(
                () -> assertThat(path.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(station1.getId(), station1.getName()),
                                tuple(station2.getId(), station2.getName()),
                                tuple(station3.getId(), station3.getName())
                        ),
                () -> assertThat(path.getDistance()).isEqualTo(4),
                () -> assertThat(path.getExtraFare()).isEqualTo(720)
        );
    }
}
