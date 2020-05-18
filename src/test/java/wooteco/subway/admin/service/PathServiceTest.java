package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundPathException;
import wooteco.subway.admin.exception.NotFoundStationException;
import wooteco.subway.admin.exception.SourceTargetSameException;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    LineService lineService;

    @Mock
    StationService stationService;

    PathService pathService;

    List<Line> lines;
    List<Station> stations;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService, stationService);

        Line line1 = new Line(1L, "1호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 4L, 0, 0));
        line1.addLineStation(new LineStation(4L, 1L, 10, 1));
        line1.addLineStation(new LineStation(1L, 2L, 10, 1));
        line1.addLineStation(new LineStation(2L, 5L, 10, 1));

        Line line2 = new Line(2L, "2호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 4L, 0, 0));
        line2.addLineStation(new LineStation(4L, 3L, 10, 10));

        Line line3 = new Line(3L, "3호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line3.addLineStation(new LineStation(null, 3L, 0, 0));
        line3.addLineStation(new LineStation(3L, 5L, 10, 10));

        Line line4 = new Line(4L, "4호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line4.addLineStation(new LineStation(null, 6L, 0, 0));

        lines = Lists.newArrayList(line1, line2, line3, line4);
        stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"), new Station(3L, "삼성역"), new Station(4L, "출발역"), new Station(5L, "도착역"), new Station(6L, "왕따역"));


    }

    @DisplayName("최단 거리 경로 구하기")
    @Test
    void findDistancePath() {
        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findByName("출발역")).thenReturn(stations.get(3));
        when(stationService.findByName("도착역")).thenReturn(stations.get(4));
        when(stationService.findAll()).thenReturn(stations);

        PathResponse pathResponse = pathService.findPath("출발역", "도착역", PathType.DISTANCE.name());
        List<StationResponse> stationResponses = pathResponse.getStations();
        assertThat(stationResponses.size()).isEqualTo(3);
        assertThat(stationResponses.get(0).getId()).isEqualTo(4L);
        assertThat(stationResponses.get(1).getId()).isEqualTo(3L);
        assertThat(stationResponses.get(2).getId()).isEqualTo(5L);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(20);
    }

    @DisplayName("최단 시간 경로 구하기")
    @Test
    void findDurationPath() {
        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findByName("출발역")).thenReturn(stations.get(3));
        when(stationService.findByName("도착역")).thenReturn(stations.get(4));
        when(stationService.findAll()).thenReturn(stations);

        PathResponse pathResponse = pathService.findPath("출발역", "도착역", PathType.DURATION.name());
        List<StationResponse> stationResponses = pathResponse.getStations();
        assertThat(stationResponses.size()).isEqualTo(4);
        assertThat(stationResponses.get(0).getId()).isEqualTo(4L);
        assertThat(stationResponses.get(1).getId()).isEqualTo(1L);
        assertThat(stationResponses.get(2).getId()).isEqualTo(2L);
        assertThat(stationResponses.get(3).getId()).isEqualTo(5L);
        assertThat(pathResponse.getDistance()).isEqualTo(30);
        assertThat(pathResponse.getDuration()).isEqualTo(3);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외")
    @Test
    void equalSourceTarget() {
        assertThatThrownBy(() -> pathService.findPath("출발역", "출발역", PathType.DISTANCE.name()))
                .isInstanceOf(SourceTargetSameException.class);
    }

    @DisplayName("출발역에서 도착역으로 가는 경로가 없는 경우 예외")
    @Test
    void notFoundPath() {
        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findByName("출발역")).thenReturn(stations.get(3));
        when(stationService.findByName("왕따역")).thenReturn(stations.get(5));
        when(stationService.findAll()).thenReturn(stations);

        assertThatThrownBy(() -> pathService.findPath("출발역", "왕따역", PathType.DISTANCE.name()))
                .isInstanceOf(NotFoundPathException.class);
    }

    @DisplayName("존재하지 않는 역에 대해서 조회하는 경우 예외")
    @Test
    void notFoundStation() {
        when(stationService.findByName("포비역")).thenThrow(new NotFoundStationException());

        assertThatThrownBy(() -> pathService.findPath("포비역", "준역", PathType.DISTANCE.name()))
                .isInstanceOf(NotFoundStationException.class);
    }
}
