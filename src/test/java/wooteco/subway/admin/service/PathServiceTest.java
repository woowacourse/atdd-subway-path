package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.service.errors.PathException;

import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    private PathService pathService;

    @BeforeEach()
    void setUp() {
        pathService = new PathService(stationService, lineService);
    }

    @Test
    void findPath() {
        ShortestPathResponse shortestPathResponse = new ShortestPathResponse();
        shortestPathResponse.setSource("강남역");
        shortestPathResponse.setTarget("사당역");
        shortestPathResponse.setPathType("DURATION");
        LineStation lineStation1 = new LineStation(null, 1L, 10, 10);
        LineStation lineStation2 = new LineStation(1L, 2L, 10, 10);
        Line line = new Line(1L, "1호선", LocalTime.now(), LocalTime.now(), 10);
        line.addLineStation(lineStation1);
        line.addLineStation(lineStation2);
        given(stationService.findByName("강남역")).willReturn(new Station(1L, "강남역"));
        given(stationService.findByName("사당역")).willReturn(new Station(2L, "사당역"));
        given(lineService.findAll()).willReturn(Arrays.asList(line));
        given(stationService.findAllById(anyList()))
                .willReturn(Arrays.asList(new Station(1L, "강남역"), new Station(2L, "사당역")));

        PathResponse path = pathService.findPath(shortestPathResponse);
        assertThat(path.getDuration()).isEqualTo(10);
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
        ShortestPathResponse shortestPathResponse = new ShortestPathResponse();
        shortestPathResponse.setSource("강남역");
        shortestPathResponse.setTarget("사당역");
        shortestPathResponse.setPathType("DURATION");

        LineStation lineStation1 = new LineStation(null, 1L, 10, 10);
        LineStation lineStation2 = new LineStation(3L, 2L, 10, 10);

        Line line = new Line(1L, "1호선", LocalTime.now(), LocalTime.now(), 10);
        line.addLineStation(lineStation1);
        line.addLineStation(lineStation2);

        given(stationService.findByName("강남역")).willReturn(new Station(1L, "강남역"));
        given(stationService.findByName("사당역")).willReturn(new Station(2L, "사당역"));
        given(lineService.findAll()).willReturn(Arrays.asList(line));

        assertThatThrownBy(() -> pathService.findPath(shortestPathResponse))
                .isInstanceOf(PathException.class)
                .hasMessage("역이 연결되있지 않습니다.");
    }
}