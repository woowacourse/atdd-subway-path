package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.common.exception.InvalidSubwayPathException;
import wooteco.subway.admin.line.domain.lineStation.LineStation;
import wooteco.subway.admin.line.repository.lineStation.LineStationRepository;
import wooteco.subway.admin.path.service.PathService;
import wooteco.subway.admin.path.service.dto.PathRequest;
import wooteco.subway.admin.path.service.dto.PathResponse;
import wooteco.subway.admin.station.domain.Station;
import wooteco.subway.admin.station.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private static final String DISTANCE_PATH_TYPE = "DISTANCE";
    private static final String DURATION_PATH_TYPE = "DURATION";

    @Mock
    LineStationRepository lineStationRepository;

    @Mock
    StationRepository stationRepository;

    private PathService pathService;
    private List<Station> stations;
    private List<LineStation> lineStations;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineStationRepository, stationRepository);

        station1 = new Station(1L, "교대");
        station2 = new Station(2L, "강남");
        station3 = new Station(3L, "양재");
        station4 = new Station(4L, "남부터미널");
        station5 = new Station(5L, "연결X");
        stations = Arrays.asList(station1, station2, station3, station4, station5);

        lineStations = new ArrayList<>();
        lineStations.add(new LineStation(null, station1.getId(), 0, 0));
        lineStations.add(new LineStation(station1.getId(), station2.getId(), 3, 1));
        lineStations.add(new LineStation(null, station1.getId(), 0, 0));
        lineStations.add(new LineStation(station1.getId(), station4.getId(), 2, 1));
        lineStations.add(new LineStation(station4.getId(), station3.getId(), 2, 2));
        lineStations.add(new LineStation(null, station2.getId(), 0, 0));
        lineStations.add(new LineStation(station2.getId(), station3.getId(), 2, 1));
    }

    @Test
    void searchPath_byDistance() {
        when(lineStationRepository.findAll()).thenReturn(lineStations);
        when(stationRepository.findAll()).thenReturn(stations);

        final PathRequest request = new PathRequest(station1.getId(), station3.getId(), DISTANCE_PATH_TYPE);

        PathResponse response = pathService.searchPath(request);

        assertThat(response.getStations()).hasSize(3);
        assertThat(response.getDistance()).isEqualTo(4);
        assertThat(response.getDuration()).isEqualTo(3);
    }

    @Test
    void searchPath_byDuration() {
        when(lineStationRepository.findAll()).thenReturn(lineStations);
        when(stationRepository.findAll()).thenReturn(stations);

        final PathRequest request = new PathRequest(station1.getId(), station3.getId(), DURATION_PATH_TYPE);

        PathResponse response = pathService.searchPath(request);

        assertThat(response.getStations()).hasSize(3);
        assertThat(response.getDistance()).isEqualTo(5);
        assertThat(response.getDuration()).isEqualTo(2);
    }

    @Test
    void searchPath_NotExistSourceStation() {
        when(lineStationRepository.findAll()).thenReturn(lineStations);
        when(stationRepository.findAll()).thenReturn(stations);

        final PathRequest request = new PathRequest(6L, station3.getId(), DISTANCE_PATH_TYPE);

        assertThatThrownBy(() -> pathService.searchPath(request))
            .isInstanceOf(InvalidSubwayPathException.class)
            .hasMessage("출발역이 존재하지 않습니다.");
    }

    @Test
    void searchPath_NotExistTargetStation() {
        when(lineStationRepository.findAll()).thenReturn(lineStations);
        when(stationRepository.findAll()).thenReturn(stations);

        final PathRequest request = new PathRequest(station1.getId(), 6L, DISTANCE_PATH_TYPE);

        assertThatThrownBy(() -> pathService.searchPath(request))
            .isInstanceOf(InvalidSubwayPathException.class)
            .hasMessage("도착역이 존재하지 않습니다.");
    }

    @Test
    void searchPath_SameSourceAndTarget() {
        when(lineStationRepository.findAll()).thenReturn(lineStations);
        when(stationRepository.findAll()).thenReturn(stations);

        final PathRequest request = new PathRequest(station1.getId(), station1.getId(), DISTANCE_PATH_TYPE);

        assertThatThrownBy(() -> pathService.searchPath(request))
            .isInstanceOf(InvalidSubwayPathException.class)
            .hasMessage("출발역과 도착역이 같습니다.");
    }

}
