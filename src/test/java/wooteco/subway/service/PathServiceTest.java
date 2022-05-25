package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.line.JdbcLineDao;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.dao.section.JdbcSectionDao;
import wooteco.subway.dao.section.SectionDao;
import wooteco.subway.dao.station.JdbcStationDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

@JdbcTest
class PathServiceTest {

    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long stationId4;
    private Long stationId5;

    private PathService pathService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new JdbcStationDao(jdbcTemplate);
        stationId1 = stationDao.save(new Station("선릉역"));
        stationId2 = stationDao.save(new Station("잠실역"));
        stationId3 = stationDao.save(new Station("역삼역"));
        stationId4 = stationDao.save(new Station("강남역"));
        stationId5 = stationDao.save(new Station("교대역"));

        LineDao lineDao = new JdbcLineDao(jdbcTemplate);
        long lineId1 = lineDao.save(new Line("2호선", "bg-green-600", 0));
        long lineId2 = lineDao.save(new Line("다른2호선", "bg-green-800", 500));

        SectionDao sectionDao = new JdbcSectionDao(jdbcTemplate);
        sectionDao.save(new Section(1L, lineId1, stationId1, stationId2, 2));
        sectionDao.save(new Section(2L, lineId1, stationId2, stationId3, 2));
        sectionDao.save(new Section(3L, lineId1, stationId3, stationId4, 7));
        sectionDao.save(new Section(4L, lineId2, stationId2, stationId5, 3));
        sectionDao.save(new Section(5L, lineId2, stationId5, stationId4, 4));

        StationService stationService = new StationService(stationDao);
        SectionService sectionService = new SectionService(sectionDao, stationService);
        LineService lineService = new LineService(lineDao, sectionService);
        pathService = new PathService(stationService, sectionService, lineService);
    }

    @DisplayName("추가 요금이 없는 경로를 조회한다.")
    @Test
    void findPath() {
        PathResponse pathResponse = pathService.findPath(stationId1, stationId2, 20);
        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting(StationResponse::getId, StationResponse::getName)
                        .containsExactly(
                                tuple(stationId1, "선릉역"),
                                tuple(stationId2, "잠실역")
                        ),
                () -> assertThat(pathResponse.getDistance())
                        .isEqualTo(2),
                () -> assertThat(pathResponse.getFare())
                        .isEqualTo(1250)
        );
    }

    @DisplayName("추가 요금이 있는 경로를 조회한다.")
    @Test
    void findPathWithExtraFares() {
        PathResponse pathResponse = pathService.findPath(stationId1, stationId4, 20);
        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting(StationResponse::getId, StationResponse::getName)
                        .containsExactly(
                                tuple(stationId1, "선릉역"),
                                tuple(stationId2, "잠실역"),
                                tuple(stationId5, "교대역"),
                                tuple(stationId4, "강남역")
                        ),
                () -> assertThat(pathResponse.getDistance())
                        .isEqualTo(9),
                () -> assertThat(pathResponse.getFare())
                        .isEqualTo(1750)
        );
    }

    @DisplayName("추가 요금 노선 여러개를 이용하는 경로를 조회한다.")
    @Test
    void findPathWithExtraFare() {
        StationDao stationDao = new JdbcStationDao(jdbcTemplate);
        Long stationId6 = stationDao.save(new Station("홍대역"));
        LineDao lineDao = new JdbcLineDao(jdbcTemplate);
        long lineId3 = lineDao.save(new Line("3호선", "bg-yellow-600", 700));
        SectionDao sectionDao = new JdbcSectionDao(jdbcTemplate);
        sectionDao.save(new Section(6L, lineId3, stationId5, stationId6, 7));

        PathResponse pathResponse = pathService.findPath(stationId2, stationId6, 20);
        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting(StationResponse::getId, StationResponse::getName)
                        .containsExactly(
                                tuple(stationId2, "잠실역"),
                                tuple(stationId5, "교대역"),
                                tuple(stationId6, "홍대역")
                        ),
                () -> assertThat(pathResponse.getDistance())
                        .isEqualTo(10),
                () -> assertThat(pathResponse.getFare())
                        .isEqualTo(1950)
        );
    }

    @DisplayName("어린이가 이용하는 노선의 금액을 조회한다.")
    @Test
    void findPathOfChild() {
        PathResponse pathResponse = pathService.findPath(stationId1, stationId4, 6);
        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting(StationResponse::getId, StationResponse::getName)
                        .containsExactly(
                                tuple(stationId1, "선릉역"),
                                tuple(stationId2, "잠실역"),
                                tuple(stationId5, "교대역"),
                                tuple(stationId4, "강남역")
                        ),
                () -> assertThat(pathResponse.getDistance())
                        .isEqualTo(9),
                () -> assertThat(pathResponse.getFare())
                        .isEqualTo(700)
        );
    }

    @DisplayName("청소년이 이용하는 노선의 금액을 조회한다.")
    @Test
    void findPathOfAdolescent() {
        PathResponse pathResponse = pathService.findPath(stationId1, stationId4, 13);
        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting(StationResponse::getId, StationResponse::getName)
                        .containsExactly(
                                tuple(stationId1, "선릉역"),
                                tuple(stationId2, "잠실역"),
                                tuple(stationId5, "교대역"),
                                tuple(stationId4, "강남역")
                        ),
                () -> assertThat(pathResponse.getDistance())
                        .isEqualTo(9),
                () -> assertThat(pathResponse.getFare())
                        .isEqualTo(1120)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다.")
    @Test
    void findPathSameTargetAndSource() {
        assertThatThrownBy(() -> pathService.findPath(stationId1, stationId1, 20))
                .isInstanceOf(SubwayException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }

    @DisplayName("역이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void findPathNotExistStation() {
        assertThatThrownBy(() -> pathService.findPath(100L, stationId1, 20))
                .isInstanceOf(DataNotExistException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }
}
