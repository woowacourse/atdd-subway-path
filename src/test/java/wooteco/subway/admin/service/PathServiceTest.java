package wooteco.subway.admin.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    LineService lineService;

    @Mock
    StationService stationService;

    PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService, stationService);
    }

    @DisplayName("최단 거리 경로 구하기")
    @Test
    void findPath() {
        Line line1 = new Line(1L, "2호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 4L, 0, 0));
        line1.addLineStation(new LineStation(4L, 1L, 10, 10));
        line1.addLineStation(new LineStation(1L, 2L, 10, 10));
        line1.addLineStation(new LineStation(2L, 5L, 10, 10));

        Line line2 = new Line(2L, "2호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 4L, 0, 0));
        line2.addLineStation(new LineStation(4L, 3L, 10, 10));

        Line line3 = new Line(3L, "2호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3.addLineStation(new LineStation(null, 3L, 0, 0));
        line3.addLineStation(new LineStation(3L, 5L, 10, 10));

        List<Line> lines = Lists.newArrayList(line1, line2, line3);

        List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"), new Station(3L, "삼성역"), new Station(4L, "출발역"), new Station(5L, "도착역"));

        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findByName("출발역")).thenReturn(stations.get(3));
        when(stationService.findByName("도착역")).thenReturn(stations.get(4));
        when(stationService.findAll()).thenReturn(stations);

        PathResponse pathResponse = pathService.findPath("출발역", "도착역", "distance");
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
    void findPath2() {
        Line line1 = new Line(1L, "2호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 4L, 0, 0));
        line1.addLineStation(new LineStation(4L, 1L, 10, 1));
        line1.addLineStation(new LineStation(1L, 2L, 10, 1));
        line1.addLineStation(new LineStation(2L, 5L, 10, 1));

        Line line2 = new Line(2L, "2호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 4L, 0, 0));
        line2.addLineStation(new LineStation(4L, 3L, 10, 10));

        Line line3 = new Line(3L, "2호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3.addLineStation(new LineStation(null, 3L, 0, 0));
        line3.addLineStation(new LineStation(3L, 5L, 10, 10));

        List<Line> lines = Lists.newArrayList(line1, line2, line3);

        List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"), new Station(3L, "삼성역"), new Station(4L, "출발역"), new Station(5L, "도착역"));

        when(lineService.findLines()).thenReturn(lines);
        when(stationService.findByName("출발역")).thenReturn(stations.get(3));
        when(stationService.findByName("도착역")).thenReturn(stations.get(4));
        when(stationService.findAll()).thenReturn(stations);

        PathResponse pathResponse = pathService.findPath("출발역", "도착역", "duration");
        List<StationResponse> stationResponses = pathResponse.getStations();
        assertThat(stationResponses.size()).isEqualTo(4);
        assertThat(stationResponses.get(0).getId()).isEqualTo(4L);
        assertThat(stationResponses.get(1).getId()).isEqualTo(1L);
        assertThat(stationResponses.get(2).getId()).isEqualTo(2L);
        assertThat(stationResponses.get(3).getId()).isEqualTo(5L);
        assertThat(pathResponse.getDistance()).isEqualTo(30);
        assertThat(pathResponse.getDuration()).isEqualTo(3);
    }
}
