package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.utils.StationFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.utils.LineFixture.*;
import static wooteco.subway.utils.SectionFixture.*;
import static wooteco.subway.utils.StationFixture.*;

@SpringBootTest
@Sql(scripts = {"classpath:schema-reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PathServiceTest {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    private final PathService pathService;

    PathServiceTest(LineDao lineDao, StationDao stationDao, SectionDao sectionDao,
                    PathService pathService) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.pathService = pathService;
    }

    @BeforeEach
    public void setUp() {
        createStations();

        createLine1();
        createLine2();
        createLine3();
        createLine4();
    }

    private void createLine1() {
        Long lineId1 = lineDao.save(LINE1);
        sectionDao.save(LINE1_SECTION1, lineId1);
        sectionDao.save(LINE1_SECTION2, lineId1);
        sectionDao.save(LINE1_SECTION3, lineId1);
    }

    private void createLine2() {
        Long lineId2 = lineDao.save(LINE2);
        sectionDao.save(LINE2_SECTION1, lineId2);
        sectionDao.save(LINE2_SECTION2, lineId2);
        sectionDao.save(LINE2_SECTION3, lineId2);
    }

    private void createLine3() {
        Long lineId3 = lineDao.save(LINE3);
        sectionDao.save(LINE3_SECTION1, lineId3);
        sectionDao.save(LINE3_SECTION2, lineId3);
        sectionDao.save(LINE3_SECTION3, lineId3);
    }

    private void createLine4() {
        Long lineId4 = lineDao.save(LINE4);
        sectionDao.save(LINE4_SECTION1, lineId4);
    }

    private void createStations() {
        List<Station> stations = List.of(STATION1, STATION2, STATION3, STATION4, STATION5, STATION6, STATION7, STATION8, STATION9, STATION10, STATION11);
        for (Station station : stations) {
            Station save = stationDao.save(station);
            System.out.println(save.getId());
        }
    }

    @Test
    @DisplayName("최단거리 경로를 반환한다.")
    void findShortestPath() {
        PathRequest pathRequest = new PathRequest(STATION1.getId(), STATION9.getId(), 20);
        PathResponse pathResponse = pathService.findShortestPath(pathRequest);
        assertThat(pathResponse.getStations()).containsExactly(
                StationResponse.from(STATION1),
                StationResponse.from(STATION2),
                StationResponse.from(STATION3),
                StationResponse.from(STATION8),
                StationResponse.from(STATION9)
        );
        assertThat(pathResponse.getDistance()).isEqualTo(58);
        assertThat(pathResponse.getFare()).isEqualTo(3050);
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 예외를 던져야 한다.")
    void findSameStationsPath() {
        PathRequest pathRequest = new PathRequest(STATION1.getId(), STATION1.getId(), 0);
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                .hasMessage("출발역과 도착역이 동일합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선 추가 요금을 더해서 반환한다.")
    void findShortestPath_lineFare() {
        PathRequest pathRequest = new PathRequest(STATION1.getId(), STATION5.getId(), 20);
        PathResponse pathResponse = pathService.findShortestPath(pathRequest);
        assertThat(pathResponse.getStations()).containsExactly(
                StationResponse.from(STATION1),
                StationResponse.from(STATION2),
                StationResponse.from(STATION5)
        );
        assertThat(pathResponse.getDistance()).isEqualTo(9);
        assertThat(pathResponse.getFare()).isEqualTo(1750);
    }
}
