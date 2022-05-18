package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dao.section.InmemorySectionDao;
import wooteco.subway.dao.station.InmemoryStationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.FindDijkstraShortestPathStrategy;
import wooteco.subway.dto.path.PathFindRequest;
import wooteco.subway.dto.path.PathFindResponse;

class PathServiceTest {

    private final InmemorySectionDao sectionDao = InmemorySectionDao.getInstance();
    private final InmemoryStationDao stationDao = InmemoryStationDao.getInstance();
    private final PathService pathService = new PathService(sectionDao, stationDao,
            new FindDijkstraShortestPathStrategy());

    @AfterEach
    void afterEach() {
        sectionDao.clear();
        stationDao.clear();
    }

    @Test
    @DisplayName("경로를 조회할 수 있다.")
    void findPath() {
        // given
        Station station1 = stationDao.findById(stationDao.save(new Station("오리")));
        Station station2 = stationDao.findById(stationDao.save(new Station("배카라")));
        Station station3 = stationDao.findById(stationDao.save(new Station("오카라")));
        Station station4 = stationDao.findById(stationDao.save(new Station("레넌")));

        sectionDao.save(new Section(1L, station1, station2, 2));
        sectionDao.save(new Section(1L, station2, station3, 2));
        sectionDao.save(new Section(2L, station1, station4, 3));
        sectionDao.save(new Section(2L, station4, station3, 3));

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
                () -> assertThat(path.getFare()).isEqualTo(1250)
        );
    }
}
