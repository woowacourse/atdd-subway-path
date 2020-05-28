package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.path.GraphStrategy;
import wooteco.subway.admin.domain.path.PathType;
import wooteco.subway.admin.domain.path.SubwayGraphStrategy;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.exception.DuplicatedValueException;
import wooteco.subway.admin.exception.UnreachablePathException;
import wooteco.subway.admin.exception.ValueRequiredException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private static final GraphStrategy SUBWAY_GRAPH_STRATEGY = new SubwayGraphStrategy();

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private PathService pathService;
    private Station kangnam;
    private Station yeoksam;
    private Station seolleung;
    private Station gyodae;
    private Station jamwon;
    private Station sinsa;
    private Line firstLine;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        seolleung = new Station(2L, "선릉역", LocalDateTime.now());
        yeoksam = new Station(3L, "역삼역", LocalDateTime.now());
        kangnam = new Station(1L, "강남역", LocalDateTime.now());
        gyodae = new Station(4L, "교대역", LocalDateTime.now());
        jamwon = new Station(5L, "잠원역", LocalDateTime.now());
        sinsa = new Station(6L, "신사역", LocalDateTime.now());
        firstLine = new Line(1L, "1호선", "bg-green-500", LocalTime.of(5, 30), LocalTime.of(23, 0),
            10);
    }

    @DisplayName("출발역이나 도착역을 입력하지 않은 경우")
    @Test
    void findPathNullException() {
        assertThatThrownBy(
            () -> pathService.findPath(SUBWAY_GRAPH_STRATEGY, null, 1L, PathType.DURATION.name()))
            .isInstanceOf(ValueRequiredException.class);
        assertThatThrownBy(
            () -> pathService.findPath(SUBWAY_GRAPH_STRATEGY, null, null, PathType.DISTANCE.name()))
            .isInstanceOf(ValueRequiredException.class);
    }

    @DisplayName("출발역, 도착역이 같은 경우")
    @Test
    void findPathSameStationsException() {
        assertThatThrownBy(
            () -> pathService.findPath(SUBWAY_GRAPH_STRATEGY, 1L, 1L, PathType.DISTANCE.name()))
            .isInstanceOf(DuplicatedValueException.class);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우")
    @Test
    void findPathNotExistException() {
        assertThatThrownBy(
            () -> pathService.findPath(SUBWAY_GRAPH_STRATEGY, 1L, 7L, PathType.DISTANCE.name()))
            .isInstanceOf(UnreachablePathException.class);
    }

    @DisplayName("출발역, 도착역이 연결되어 있지 않은 경우")
    @Test
    void findPathNotConnectedException() {
        firstLine.addLineStation(LineStation.of(null, jamwon.getId(), 10, 10));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine));
        assertThatThrownBy(
            () -> pathService.findPath(SUBWAY_GRAPH_STRATEGY, jamwon.getId(), yeoksam.getId(),
                PathType.DISTANCE.name()))
            .isInstanceOf(UnreachablePathException.class);
    }

    @DisplayName("source stationId와 target stationId를 받아서 최단 경로를 구한다.")
    @Test
    void findPath() {
        firstLine.addLineStation(LineStation.of(null, seolleung.getId(), 10, 10));
        firstLine.addLineStation(LineStation.of(seolleung.getId(), yeoksam.getId(), 20, 10));
        firstLine.addLineStation(LineStation.of(yeoksam.getId(), kangnam.getId(), 20, 10));
        firstLine.addLineStation(LineStation.of(kangnam.getId(), gyodae.getId(), 20, 10));
        Line secondLine = new Line(2L, "2호선", "bg-green-500", LocalTime.of(6, 30),
            LocalTime.of(23, 00), 10);
        secondLine.addLineStation(LineStation.of(null, gyodae.getId(), 10, 10));
        secondLine.addLineStation(LineStation.of(gyodae.getId(), jamwon.getId(), 40, 30));
        secondLine.addLineStation(LineStation.of(jamwon.getId(), sinsa.getId(), 30, 10));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine, secondLine));
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(seolleung, yeoksam, kangnam, gyodae, jamwon, sinsa));

        PathResponse pathResponse = pathService.findPath(SUBWAY_GRAPH_STRATEGY, 2L, 6L,
            PathType.DISTANCE.name());
        assertThat(pathResponse.getStations()).size().isEqualTo(6);
        assertThat(pathResponse.getDistance()).isEqualTo(130);
        assertThat(pathResponse.getDuration()).isEqualTo(70);
    }

    @DisplayName("최단시간과 최단거리 기준으로 path를 구할 수 있다.")
    @Test
    void findPathBy() {
        Line first = new Line(1L, "1호선", "bg-pink-600", LocalTime.of(10, 0), LocalTime.of(12, 0),
            1);
        Line second = new Line(2L, "2호선", "bg-pink-600", LocalTime.of(10, 0), LocalTime.of(12, 0),
            1);
        Line third = new Line(3L, "3호선", "bg-pink-600", LocalTime.of(10, 0), LocalTime.of(12, 0),
            1);
        first.addLineStation(LineStation.of(null, 1L, 1, 1));
        first.addLineStation(LineStation.of(1L, 3L, 1, 1));
        first.addLineStation(LineStation.of(3L, 2L, 1, 1));
        first.addLineStation(LineStation.of(2L, 4L, 1, 1));
        second.addLineStation(LineStation.of(null, 4L, 1, 1));
        second.addLineStation(LineStation.of(4L, 6L, 1, 1));
        second.addLineStation(LineStation.of(6L, 5L, 1, 1));
        third.addLineStation(LineStation.of(null, 3L, 1, 1));
        third.addLineStation(LineStation.of(3L, 6L, 300, 1));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(first, second, third));
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(jamwon, sinsa, gyodae, seolleung, yeoksam, kangnam));

        PathResponse durationResponse = pathService.findPath(SUBWAY_GRAPH_STRATEGY, 1L, 5L,
            PathType.DURATION.name());
        PathResponse distanceResponse = pathService.findPath(SUBWAY_GRAPH_STRATEGY, 1L, 5L,
            PathType.DISTANCE.name());
        assertThat(durationResponse.getStations()).size().isEqualTo(4);
        assertThat(durationResponse.getDuration()).isEqualTo(3);
        assertThat(durationResponse.getDistance()).isEqualTo(302);
        assertThat(distanceResponse.getStations()).size().isEqualTo(6);
        assertThat(distanceResponse.getDuration()).isEqualTo(5);
        assertThat(distanceResponse.getDistance()).isEqualTo(5);
    }
}