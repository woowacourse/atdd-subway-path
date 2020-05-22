package wooteco.subway.service.map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.WeightType;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "양재역";

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private MapService mapService;

    private Line line;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        mapService = new MapService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
        line.addLineStation(new LineStation(null, 1L, 0, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 1));
        line.addLineStation(new LineStation(2L, 3L, 10, 1));
        line.addLineStation(new LineStation(3L, 4L, 10, 1));

        line2 = new Line(2L, "4호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-blue-600");
        line2.addLineStation(new LineStation(1L, 5L, 10, 10));
        line2.addLineStation(new LineStation(5L, 4L, 10, 10));
    }

    @DisplayName("경로 조회시, 최단 경로 기준으로 path생성")
    @Test
    void searchPath_GivenDistanceWeight_CreatePath() {
        // given
        List<Station> stations = Arrays.asList(station1, station2, station3, station4, station5);
        when(stationRepository.findAll()).thenReturn(stations);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line, line2));

        String source = STATION_NAME1;
        String target = STATION_NAME4;
        List<StationResponse> expected = StationResponse.listOf(
            Arrays.asList(station1, station5, station4));

        //when
        PathResponse pathResponse = mapService.searchPath(source, target,
            WeightType.DISTANCE.getName());

        //then
        List<StationResponse> actual = pathResponse.getStations();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("경로 조회시, 최단 시간을 기준으로 path생성")
    @Test
    void searchPath_GivenDurationWeight_CreatePath() {
        // given
        List<Station> stations = Arrays.asList(station1, station2, station3, station4, station5);
        when(stationRepository.findAll()).thenReturn(stations);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line, line2));

        String source = STATION_NAME1;
        String target = STATION_NAME4;
        List<StationResponse> expected = StationResponse.listOf(
            Arrays.asList(station1, station2, station3, station4));

        //when
        PathResponse pathResponse = mapService.searchPath(source, target,
            WeightType.DURATION.getName());

        //then
        List<StationResponse> actual = pathResponse.getStations();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("예외테스트: 경로 조회시, 출발역과 도착역이 같은 경우 예외 발생 확인")
    @Test
    void searchPath_GivenSameStations_ExceptionThrown() {
        assertThatThrownBy(
            () -> mapService.searchPath(STATION_NAME1, STATION_NAME1,
                WeightType.DISTANCE.getName()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역은 같을 수 없습니다");
    }
}
