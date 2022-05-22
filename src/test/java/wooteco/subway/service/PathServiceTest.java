package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.acceptance.DBTest;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.DijkstraPath;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;

@SpringBootTest
class PathServiceTest extends DBTest {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineService lineService;
    private final PathService pathService;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private LineServiceResponse line;

    @Autowired
    public PathServiceTest(StationDao stationDao, SectionDao sectionDao, LineService lineService,
                           PathService pathService) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineService = lineService;
        this.pathService = pathService;
    }

    @BeforeEach
    void setUp() {
        station1 = stationDao.save(new Station("강남역"));
        station2 = stationDao.save(new Station("선릉역"));
        station3 = stationDao.save(new Station("수서역"));
        station4 = stationDao.save(new Station("가락시장역"));
        station5 = stationDao.save(new Station("천호역"));
        line = lineService.save(new LineServiceRequest(
                "2호선", "green", station1.getId(), station2.getId(), 2, 200));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 3));
    }

    @DisplayName("최단 경로의 경유역들과 거리, 운임비용을 반환한다.")
    @Test
    void findShortestPath() {
        lineService.save(new LineServiceRequest("3호선", "orange", station2.getId(), station4.getId(), 4));
        PathServiceRequest pathServiceRequest = new PathServiceRequest(station1.getId(), station4.getId(), 10);
        PathServiceResponse pathServiceResponse = pathService.findShortestPath(pathServiceRequest, DijkstraPath::new);

        assertAll(
                () -> assertThat(pathServiceResponse.getStations()).containsExactly(station1, station2, station4),
                () -> assertThat(pathServiceResponse.getDistance()).isEqualTo(6),
                () -> assertThat(pathServiceResponse.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("구간에 등록되지 않은 지하철역으로 최단 경로 조회시 예외가 발생한다.")
    @Test
    void findShortestPath_exceptionNotSavedInSection() {
        lineService.save(new LineServiceRequest("3호선", "orange", station2.getId(), station4.getId(), 4));
        PathServiceRequest pathServiceRequest = new PathServiceRequest(station1.getId(), station5.getId(), 10);

        assertThatThrownBy(() -> pathService.findShortestPath(pathServiceRequest, DijkstraPath::new))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 등록 되지 않은 역입니다.");
    }

    @DisplayName("연결되지 않은 구간의 최단 경로 조회시 예외가 발생한다.")
    @Test
    void findShortestPath_exceptionInvalidPath() {
        lineService.save(new LineServiceRequest("3호선", "orange", station5.getId(), station4.getId(), 4));
        PathServiceRequest pathServiceRequest = new PathServiceRequest(station1.getId(), station5.getId(), 10);

        assertThatThrownBy(() -> pathService.findShortestPath(pathServiceRequest, DijkstraPath::new))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }
}
