package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private static final String STATION_NAME1 = "양재시민역";
    private static final String STATION_NAME2 = "양재역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "역삼역";
    private static final String STATION_NAME5 = "강남역";

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    private Line line1;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);

        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2 = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        line1.addLineStation(new LineStation(null, 5L, 0, 0));
        line1.addLineStation(new LineStation(5L, 4L, 15, 15));
        line1.addLineStation(new LineStation(4L, 3L, 5, 5));

        line2.addLineStation(new LineStation(null, 5L, 0, 0));
        line2.addLineStation(new LineStation(5L, 2L, 15, 15));
        line2.addLineStation(new LineStation(2L, 1L, 5, 5));
    }

    @Test
    void findShortestPathByDistanceTest() {
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));

        PathResponse pathResponse = pathService.findShortestPathByDistance(station1.getId(), station4.getId());

        assertThat(pathResponse.getStations().size()).isEqualTo(4);
        assertThat(pathResponse.getDistance()).isEqualTo(35);
        assertThat(pathResponse.getDuration()).isEqualTo(35);
    }
}
