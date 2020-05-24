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
import wooteco.subway.admin.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    StationService stationService;

    @Mock
    LineRepository lineRepository;

    PathService pathService;

    List<Line> lines;
    List<Station> stations;

    @BeforeEach
    void setUp() {
        pathService = new PathService(stationService, new SubwayGraphService(lineRepository));

        Line line2 = new Line(1L, "8호선", "bg-pink-500", LocalTime.of(5, 30), LocalTime.of(22, 30),
                5);
        line2.addLineStation(new LineStation(null, 1L, 0, 0));
        line2.addLineStation(new LineStation(1L, 2L, 10, 5));
        line2.addLineStation(new LineStation(2L, 3L, 10, 3));
        line2.addLineStation(new LineStation(3L, 4L, 10, 7));
        line2.addLineStation(new LineStation(4L, 5L, 10, 2));
        line2.addLineStation(new LineStation(5L, 6L, 10, 3));
        line2.addLineStation(new LineStation(6L, 7L, 10, 8));

        Line bundang = new Line(2L, "분당선", "bg-yellow-500", LocalTime.of(5, 30),
                LocalTime.of(22, 30),
                5);
        bundang.addLineStation(new LineStation(null, 1L, 0, 0));
        bundang.addLineStation(new LineStation(1L, 8L, 10, 20));
        bundang.addLineStation(new LineStation(8L, 9L, 10, 10));
        bundang.addLineStation(new LineStation(9L, 7L, 10, 30));
        bundang.addLineStation(new LineStation(7L, 10L, 10, 10));

        lines = Lists.newArrayList(line2, bundang);
        stations = Lists.newArrayList(new Station(1L, "모란역"), new Station(2L, "수진역"),
                new Station(3L, "신흥역"), new Station(4L, "딘대오거리역"), new Station(5L, "남한산성입구역"),
                new Station(6L, "산성역"), new Station(7L, "복정역"), new Station(8L, "태평역"),
                new Station(9L, "가천대역"), new Station(10L, "수서역"), new Station(11L, "반월당역"));
    }

    @DisplayName("최단 거리 경로 구하기")
    @Test
    void findDistancePath() {
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationService.findByName("모란역")).thenReturn(stations.get(0));
        when(stationService.findByName("수서역")).thenReturn(stations.get(9));
        when(stationService.findAll()).thenReturn(stations);

        PathResponse pathResponse = pathService.findPath("모란역", "수서역", PathType.DISTANCE.name());
        List<StationResponse> stationResponses = pathResponse.getStations();
        assertThat(stationResponses.size()).isEqualTo(5);
        assertThat(stationResponses.get(0).getId()).isEqualTo(1L);
        assertThat(stationResponses.get(1).getId()).isEqualTo(8L);
        assertThat(stationResponses.get(2).getId()).isEqualTo(9L);
        assertThat(stationResponses.get(3).getId()).isEqualTo(7L);
        assertThat(stationResponses.get(4).getId()).isEqualTo(10L);
        assertThat(pathResponse.getDistance()).isEqualTo(40);
        assertThat(pathResponse.getDuration()).isEqualTo(70);
    }

    @DisplayName("최단 시간 경로 구하기")
    @Test
    void findDurationPath() {
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationService.findByName("모란역")).thenReturn(stations.get(0));
        when(stationService.findByName("수서역")).thenReturn(stations.get(9));
        when(stationService.findAll()).thenReturn(stations);

        PathResponse pathResponse = pathService.findPath("모란역", "수서역", PathType.DURATION.name());
        List<StationResponse> stationResponses = pathResponse.getStations();
        assertThat(stationResponses.size()).isEqualTo(8);
        assertThat(stationResponses.get(0).getId()).isEqualTo(1L);
        assertThat(stationResponses.get(1).getId()).isEqualTo(2L);
        assertThat(stationResponses.get(2).getId()).isEqualTo(3L);
        assertThat(stationResponses.get(3).getId()).isEqualTo(4L);
        assertThat(stationResponses.get(4).getId()).isEqualTo(5L);
        assertThat(stationResponses.get(5).getId()).isEqualTo(6L);
        assertThat(stationResponses.get(6).getId()).isEqualTo(7L);
        assertThat(stationResponses.get(7).getId()).isEqualTo(10L);
        assertThat(pathResponse.getDistance()).isEqualTo(70);
        assertThat(pathResponse.getDuration()).isEqualTo(38);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외")
    @Test
    void equalSourceTarget() {
        assertThatThrownBy(() -> pathService.findPath("모란역", "모란역", PathType.DISTANCE.name()))
                .isInstanceOf(SourceTargetSameException.class);
    }

    @DisplayName("출발역에서 도착역으로 가는 경로가 없는 경우 예외")
    @Test
    void notFoundPath() {
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationService.findByName("모란역")).thenReturn(stations.get(0));
        when(stationService.findByName("반월당역")).thenReturn(stations.get(10));

        assertThatThrownBy(() -> pathService.findPath("모란역", "반월당역", PathType.DISTANCE.name()))
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
