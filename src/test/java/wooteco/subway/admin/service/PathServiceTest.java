package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathAlgorithm;
import wooteco.subway.admin.domain.PathAlgorithmByDijkstra;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.IllegalStationNameException;
import wooteco.subway.admin.exception.NotFoundLineException;
import wooteco.subway.admin.exception.NotFoundPathException;
import wooteco.subway.admin.exception.NotFoundStationException;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private static final String STATION_NAME1 = "잠실역";
    private static final String STATION_NAME2 = "잠실새내역";
    private static final String STATION_NAME3 = "종합운동장역";
    private static final String STATION_NAME4 = "삼전역";
    private static final String STATION_NAME5 = "석촌고분역";
    private static final String STATION_NAME6 = "석촌역";
    private static final String STATION_NAME7 = "부산역";
    private static final String STATION_NAME8 = "대구역";
    private static final String STATION_NAME9 = "런던역";

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    private PathAlgorithm pathAlgorithm = new PathAlgorithmByDijkstra();

    private PathService pathService;

    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private Station station7;
    private Station station8;
    private Station station9;

    private List<Line> lines;
    private List<Station> stations;

    @BeforeEach
    void setUp() {
        pathService = new PathService(stationService, lineService, pathAlgorithm);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);
        station6 = new Station(6L, STATION_NAME6);
        station7 = new Station(7L, STATION_NAME7);
        station8 = new Station(8L, STATION_NAME8);
        station9 = new Station(9L, STATION_NAME8);

        line1 = Line.of(1L, "2호선", "bg-green-400", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 0, 0));
        line1.addLineStation(new LineStation(1L, 2L, 10, 1));
        line1.addLineStation(new LineStation(2L, 3L, 10, 1));
        line1.addLineStation(new LineStation(3L, 4L, 10, 1));

        line2 = Line.of(2L, "8호선", "bg-pink-600", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 1L, 1, 10));
        line2.addLineStation(new LineStation(1L, 6L, 1, 10));

        line3 = Line.of(3L, "9호선", "bg-yellow-700", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3.addLineStation(new LineStation(null, 3L, 0, 0));
        line3.addLineStation(new LineStation(3L, 4L, 1, 10));
        line3.addLineStation(new LineStation(4L, 5L, 1, 10));
        line3.addLineStation(new LineStation(5L, 6L, 1, 10));

        line4 = Line.of(4L, "대구1호선", "bg-indigo-500", LocalTime.of(05, 30), LocalTime.of(22, 30),
            5);
        line4.addLineStation(new LineStation(null, 7L, 0, 0));
        line4.addLineStation(new LineStation(7L, 8L, 20, 20));

        stations = Arrays.asList(station1, station2, station3, station4, station5, station6,
            station7, station8);
        lines = Arrays.asList(line1, line2, line3, line4);
    }

    @Test
    void findPathForNextStation() {
        PathRequest request = new PathRequest(station1.getName(),
            station2.getName(), PathType.DISTANCE.name());

        when(stationService.findIdByName(station1.getName())).thenReturn(station1.getId());
        when(stationService.findIdByName(station2.getName())).thenReturn(station2.getId());
        when(lineService.findAll()).thenReturn(lines);
        when(stationService.findAllById(anyList())).thenReturn(Arrays.asList(station1, station2));
        PathResponse path = pathService.findPath(request);

        assertThat(path.getStations()).extracting(StationResponse::getName)
            .containsExactly(station1.getName(),
                station2.getName());
        assertThat(path.getTotalDistance()).isEqualTo(10);
        assertThat(path.getTotalDuration()).isEqualTo(1);
    }

    @Test
    void findPathForNotConnectedStation() {
        PathRequest request = new PathRequest(station1.getName(),
            station8.getName(), PathType.DISTANCE.name());

        when(stationService.findIdByName(station1.getName())).thenReturn(station1.getId());
        when(stationService.findIdByName(station8.getName())).thenReturn(station8.getId());
        when(lineService.findAll()).thenReturn(lines);
        assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(NotFoundPathException.class);
    }

    @Test
    void findPathForSameStation() {
        PathRequest request = new PathRequest(station1.getName(),
            station1.getName(), PathType.DISTANCE.name());

        when(lineService.findAll()).thenReturn(lines);
        when(stationService.findIdByName(anyString())).thenReturn(station1.getId());
        assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(IllegalStationNameException.class);
    }

    @Test
    void findPathForNotExistStation() {
        PathRequest request = new PathRequest(station1.getName(), station9.getName(),
            PathType.DISTANCE.name());

        when(stationService.findIdByName(station1.getName())).thenReturn(station1.getId());
        when(stationService.findIdByName(station9.getName()))
            .thenThrow(new NotFoundStationException());
        assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(NotFoundStationException.class);
    }

    @Test
    void findPathForNotExistLines() {
        PathRequest request = new PathRequest(station1.getName(), station2.getName(),
            PathType.DISTANCE.name());
        when(stationService.findIdByName(station1.getName())).thenReturn(station1.getId());
        when(stationService.findIdByName(station2.getName())).thenReturn(station2.getId());
        when(lineService.findAll()).thenReturn(null);
        assertThatThrownBy(() -> pathService.findPath(request)).isInstanceOf(
            NotFoundLineException.class);
    }
}