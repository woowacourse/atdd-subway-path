package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@SpringBootTest
@Transactional
@Sql("/path.sql")
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        Station 잠실역 = stationDao.save(new Station("잠실역"));
        Station 선릉역 = stationDao.save(new Station("선릉역"));
        Station 마라도 = stationDao.save(new Station("마라도역"));
        Station 독도역 = stationDao.save(new Station("독도역"));
        Line 신분당선 = lineDao.save(new Line("2호선", "bg-green-600", 0));
        sectionDao.save(신분당선.getId(), 잠실역.getId(), 선릉역.getId(), 12);
        sectionDao.save(신분당선.getId(), 마라도.getId(), 독도역.getId(), 3);
    }

    @Test
    @DisplayName("출발지에서 목적지로 갈 수 있는 최단 경로와 최단 거리, 요금을 계산한다.")
    void findPath() {
        PathResponse pathResponse = pathService.findPath(1L, 2L, 0);
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1350)
        );
    }

    @Test
    @DisplayName("출발지에서 목적지 중 존재하지 않는 지하철역일 경우 예외를 발생시킨다.")
    void noExistStation() {
        assertThatThrownBy(() -> pathService.findPath(1L, 312L, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발지, 도착지 모두 존재해야 됩니다.");
    }

    @Test
    @DisplayName("출발지에서 도착지로 갈 수 없는 경우 예외를 발생시킨다.")
    void noReachable() {
        assertThatThrownBy(() -> pathService.findPath(1L, 3L, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발지에서 도착지로 갈 수 없습니다.");
    }
}
