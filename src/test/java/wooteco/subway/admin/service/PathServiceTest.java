package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.errors.PathException;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PathServiceTest {

    @Autowired
    StationRepository stationRepository;
    @Autowired
    LineRepository lineRepository;
    @Autowired
    private StationService stationService;
    @Autowired
    private LineService lineService;
    private PathService pathService;

    @BeforeEach()
    void setUp() {
        stationRepository.deleteAll();
        lineRepository.deleteAll();
        pathService = new PathService(stationService, lineService);
    }

    @Test
    void findPath() {
        Station source = stationService.save(new Station("강남역"));
        Station middle = stationService.save(new Station("사당역"));
        Station target = stationService.save(new Station("홍대입구역"));
        String pathType = "DISTANCE";

        ShortestPathResponse shortestPathResponse = new ShortestPathResponse(source.getName(), target.getName(), pathType);

        Line line = new Line("1호선", LocalTime.now(), LocalTime.now(), 10);
        line.addLineStation(new LineStation(null, source.getId(), 10, 10));
        line.addLineStation(new LineStation(source.getId(), middle.getId(), 10, 10));
        line.addLineStation(new LineStation(middle.getId(), target.getId(), 10, 10));
        lineService.save(line);

        PathService pathService = new PathService(stationService, lineService);
        PathResponse pathResponse = pathService.findPath(shortestPathResponse);

        assertThat(pathResponse.getStations().size()).isEqualTo(3);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
    }

    @Test
    void sameStationNameTest() {
        String source = "강남역";
        String target = "강남역";
        String pathType = "DISTANCE";

        ShortestPathResponse shortestPathResponse = new ShortestPathResponse(source, target, pathType);

        assertThatThrownBy(() -> pathService.findPath(shortestPathResponse))
                .isInstanceOf(PathException.class).hasMessage("출발역과 도착역은 같은 지하철역이 될 수 없습니다.");
    }

    @Test
    void sourceAndTargetNotLinked() {
        Station sourceStation = stationService.save(new Station("강남역"));
        Station targetStation = stationService.save(new Station("잠실역"));

        LineStation lineStation = new LineStation(null, sourceStation.getId(), 10, 10);
        LineStation lineStation2 = new LineStation(sourceStation.getId() + 1L, targetStation.getId(), 10, 10);

        Line line = new Line("1호선", LocalTime.now(), LocalTime.now(), 10);
        line.addLineStation(lineStation);
        line.addLineStation(lineStation2);
        lineService.save(line);

        ShortestPathResponse shortestPathResponse = new ShortestPathResponse(sourceStation.getName(), targetStation.getName(), "DISTANCE");

        assertThatThrownBy(() -> pathService.findPath(shortestPathResponse))
                .isInstanceOf(PathException.class)
                .hasMessage("역이 연결되있지 않습니다.");
    }

    @Test
    void sourceAndTargetNotfound() {
        String source = "강남역";
        String target = "잠실역";
        String pathType = "DISTANCE";

        ShortestPathResponse shortestPathResponse = new ShortestPathResponse(source, target, pathType);
        PathService pathService = new PathService(stationService, lineService);

        assertThatThrownBy(() -> pathService.findPath(shortestPathResponse))
                .isInstanceOf(PathException.class)
                .hasMessage("해당 역을 찾을 수 없습니다.");
    }
}