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
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.NoExistPathTypeException;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";

    private List<Station> stations;
    private Line line;
    private Line line2;

    @Mock
    private LineService lineService;
    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService);
        stations = Arrays.asList(new Station(1L, STATION_NAME1), new Station(2L, STATION_NAME2),
            new Station(3L, STATION_NAME3));

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
        line.addLineStation(new LineStation(null, 1L, 5, 10));
        line.addLineStation(new LineStation(1L, 2L, 5, 10));
        line.addLineStation(new LineStation(2L, 3L, 5, 10));
        line2 = new Line(2L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-orange-600");
        line2.addLineStation(new LineStation(null, 1L, 15, 10));
        line2.addLineStation(new LineStation(1L, 3L, 15, 10));
    }

    @DisplayName("타입에 따른 최단 경로 조회")
    @ParameterizedTest
    @MethodSource("createDistancePathAndDurationPath")
    void searchShortestDistancePath(String type, int size, int distance, int duration) {
        when(lineService.findStationByName(STATION_NAME1)).thenReturn(stations.get(0));
        when(lineService.findStationByName(STATION_NAME3)).thenReturn(stations.get(2));
        when(lineService.showLines()).thenReturn(Arrays.asList(line, line2));
        when(lineService.findAllStationsByIds(anyList())).thenReturn(stations.subList(0, size));

        PathResponse pathResponse = pathService.searchShortestPath(STATION_NAME1, STATION_NAME3,
            type);

        assertThat(pathResponse.getStations().size()).isEqualTo(size);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getDuration()).isEqualTo(duration);
    }

    static Stream<Arguments> createDistancePathAndDurationPath() {
        return Stream.of(
            Arguments.of("DISTANCE", 3, 10, 20),
            Arguments.of("DURATION", 2, 15, 10)
        );
    }

    @DisplayName("잘못된 타입 에러")
    @Test
    void searchPathWithInvalidType() {
        when(lineService.findStationByName(STATION_NAME1)).thenReturn(stations.get(0));
        when(lineService.findStationByName(STATION_NAME3)).thenReturn(stations.get(2));

        assertThatThrownBy(() -> pathService.searchShortestPath(STATION_NAME1, STATION_NAME3,
            "aaa"))
            .isInstanceOf(NoExistPathTypeException.class)
            .hasMessage("존재하지 않는 경로 타입입니다.");
    }
}