package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.NotFoundResourceException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;
import wooteco.subway.admin.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
class PathAlgorithmServiceTest {
    public static final String STATION_NAME1 = "강남역";
    public static final String STATION_NAME2 = "역삼역";
    public static final String STATION_NAME3 = "선릉역";
    public static final String STATION_NAME4 = "삼성역";
    public static final String STATION_NAME5 = "자갈치시장역";
    public static final Long FIRST_PRE_STATION_ID = null;
    private static final String DISTANCE = "DISTANCE";
    private static final String DURATION = "DURATION";

    private PathAlgorithmService pathAlgorithmService;

    @Mock
    private LineRepository lineRepository;

    private List<Line> lines;
    private Stations stations;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        pathAlgorithmService = new JGraphAlgorithmService(lineRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);
        stations = new Stations(Arrays.asList(station1, station2, station3, station4, station5));

        Line lineOne = new Line(1L, "수도권 1호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        Line lineTwo = new Line(2L, "수도권 2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        Line busanLineOne = new Line(3L, "부산 1호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);

        lines = Arrays.asList(lineOne, lineTwo, busanLineOne);

        lineOne.addLineStation(new LineStation(FIRST_PRE_STATION_ID, station1.getId(), 0, 0));
        lineOne.addLineStation(new LineStation(station1.getId(), station2.getId(), 5, 3));
        lineOne.addLineStation(new LineStation(station2.getId(), station3.getId(), 5, 2));
        lineOne.addLineStation(new LineStation(station3.getId(), station4.getId(), 3, 4));

        lineTwo.addLineStation(new LineStation(FIRST_PRE_STATION_ID, station4.getId(), 0, 0));
        lineTwo.addLineStation(new LineStation(station4.getId(), station2.getId(), 9, 1));

        busanLineOne.addLineStation(new LineStation(FIRST_PRE_STATION_ID, station5.getId(), 0, 0));
    }

    @DisplayName("최단거리 경로를 잘 찾아내는지 확인")
    @Test
    void shortestDistancePath() {
        when(lineRepository.findAll()).thenReturn(lines);

        ShortestPathResponse expected = new ShortestPathResponse(
            StationResponse.listOf(Arrays.asList(station1, station2, station3, station4)), 13, 9
        );
        assertThat(pathAlgorithmService.findShortestPath(new PathRequest(STATION_NAME1, STATION_NAME4, DISTANCE), stations)).isEqualTo(expected);
    }

    @DisplayName("최단시간 경로를 잘 찾아내는지 확인")
    @Test
    void shortestDurationPath() {
        when(lineRepository.findAll()).thenReturn(lines);

        ShortestPathResponse expected = new ShortestPathResponse(
            StationResponse.listOf(Arrays.asList(station1, station2, station4)), 14, 4
        );
        assertThat(pathAlgorithmService.findShortestPath(new PathRequest(STATION_NAME1, STATION_NAME4, DURATION), stations)).isEqualTo(expected);
    }

    @DisplayName("출발/도착역이 노선에 포함되어있지 않는 경우, NoSuchStationException을 반환한다.")
    @ParameterizedTest
    @MethodSource("nonIncludingSourceTargetTypeSet")
    void shortestDurationPathNotConcludingStation(String source, String target, String type) {
        assertThatThrownBy(() -> pathAlgorithmService.findShortestPath(new PathRequest(source, target, type), stations))
            .isInstanceOf(NotFoundResourceException.class);
    }

    //@formatter:off
    private static Stream<Arguments> nonIncludingSourceTargetTypeSet() {
        final String nonIncludingStation = "EMPTY";
        final String nonIncludingStation2 = "EMPTY2";
        return Stream.of(
            Arguments.of(nonIncludingStation, STATION_NAME1, DISTANCE),
            Arguments.of(nonIncludingStation, STATION_NAME1, DURATION),
            Arguments.of(STATION_NAME1, nonIncludingStation, DISTANCE),
            Arguments.of(STATION_NAME1, nonIncludingStation, DURATION),
            Arguments.of(STATION_NAME1, nonIncludingStation, DISTANCE),
            Arguments.of(STATION_NAME1, nonIncludingStation, DURATION),
            Arguments.of(nonIncludingStation, nonIncludingStation2, DURATION),
            Arguments.of(nonIncludingStation, nonIncludingStation2, DISTANCE),
            Arguments.of(nonIncludingStation, nonIncludingStation2, DURATION),
            Arguments.of(nonIncludingStation, nonIncludingStation2, DISTANCE)
        );
    }
    //@formatter:on

    @DisplayName("출발/도착역이 같은 경우, SameSourceAndDestinationException을 반환한다.")
    @ParameterizedTest
    @CsvSource({"DISTANCE", "DURATION"})
    void shortestDurationPathWithSameSourceTarget(String type) {
        when(lineRepository.findAll()).thenReturn(lines);

        assertThatThrownBy(() -> pathAlgorithmService.findShortestPath(new PathRequest(STATION_NAME1, STATION_NAME1, type), stations))
            .isInstanceOf(SameSourceAndDestinationException.class);
    }

    @DisplayName("출발/도착역이 연결되어있지 않는 노선인 경우, DisconnectedPathException 을 반환한다.")
    @ParameterizedTest
    @CsvSource({"DISTANCE", "DURATION"})
    void shortestDurationPathDisconnectedPath(String type) {
        when(lineRepository.findAll()).thenReturn(lines);

        assertThatThrownBy(() -> pathAlgorithmService.findShortestPath(new PathRequest(STATION_NAME1, STATION_NAME5, type), stations))
            .isInstanceOf(DisconnectedPathException.class);
    }
}