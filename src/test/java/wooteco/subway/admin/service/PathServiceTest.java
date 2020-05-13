package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME_1 = "선릉";
    private static final String STATION_NAME_2 = "역삼";
    private static final String STATION_NAME_3 = "강남";
    private static final String STATION_NAME_4 = "양재";
    private static final String STATION_NAME_5 = "양재시민의숲";

    private static final String LINE_NAME_1 = "2호선";
    private static final String LINE_NAME_2 = "신분당선";

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    private Line line1;
    private Line line2;

    private LineStation lineStation1;
    private LineStation lineStation2;
    private LineStation lineStation3;
    private LineStation lineStation4;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, STATION_NAME_1);
        station2 = new Station(2L, STATION_NAME_2);
        station3 = new Station(3L, STATION_NAME_3);
        station4 = new Station(4L, STATION_NAME_4);
        station5 = new Station(5L, STATION_NAME_5);

        line1 = new Line(1L, LINE_NAME_1, LocalTime.now(), LocalTime.now(), 10, "black");
        line2 = new Line(2L, LINE_NAME_2, LocalTime.now(), LocalTime.now(), 10, "red");

        lineStation1 = new LineStation(1L, 2L, 10, 10);
        lineStation2 = new LineStation(2L, 3L, 10, 10);
        lineStation3 = new LineStation(3L, 4L, 10, 10);
        lineStation4 = new LineStation(4L, 5L, 10, 10);

        pathService = new PathService(lineRepository, stationRepository);

        line1.addLineStation(lineStation1);
        line1.addLineStation(lineStation2);
        line2.addLineStation(lineStation3);
        line2.addLineStation(lineStation4);
    }

    @Test
    public void findShortestPathTest() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5));

        PathResponse shortestPath = pathService.findShortestPath(STATION_NAME_1, STATION_NAME_5);

        assertThat(shortestPath.getStations().size()).isEqualTo(5);
        assertThat(shortestPath.getDistance()).isEqualTo(40);
        assertThat(shortestPath.getDuration()).isEqualTo(40);
    }
}
