package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.Graphs;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    private Graphs graphs = new Graphs();

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
        pathService = new PathService(lineRepository, stationRepository, graphs);

        seolleung = new Station(2L, "선릉역", LocalDateTime.now());
        yeoksam = new Station(3L, "역삼역", LocalDateTime.now());
        kangnam = new Station(1L, "강남역", LocalDateTime.now());
        gyodae = new Station(4L, "교대역", LocalDateTime.now());
        jamwon = new Station(5L, "잠원역", LocalDateTime.now());
        sinsa = new Station(6L, "신사역", LocalDateTime.now());
        firstLine = Line.of(1L, "1호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(23, 00),
            10);
    }

    @DisplayName("출발역이나 도착역을 입력하지 않은 경우")
    @Test
    void findPathNullException() {
        assertThatThrownBy(() -> pathService.findPath(null, 1L, PathType.DURATION))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> pathService.findPath(null, null, PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역, 도착역이 같은 경우")
    @Test
    void findPathSameStationsException() {
        assertThatThrownBy(() -> pathService.findPath(1L, 1L, PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findPathNotExistException() {
        assertThatThrownBy(() -> pathService.findPath(1L, 7L, PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역, 도착역이 연결되어 있지 않은 경우")
    @Test
    void findPathNotConnectedException() {
        firstLine.addLineStation(LineStation.of(null, jamwon.getId(), 10, 10));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(jamwon, yeoksam));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine));
        graphs.initialize(lineRepository.findAll(), stationRepository.findAll());
        assertThatThrownBy(
            () -> pathService.findPath(jamwon.getId(), yeoksam.getId(), PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("source stationId와 target stationId를 받아서 최단 경로를 구한다.")
    @Test
    void findPath() {
        firstLine.addLineStation(LineStation.of(null, seolleung.getId(), 10, 10));
        firstLine.addLineStation(LineStation.of(seolleung.getId(), yeoksam.getId(), 20, 10));
        firstLine.addLineStation(LineStation.of(yeoksam.getId(), kangnam.getId(), 20, 10));
        firstLine.addLineStation(LineStation.of(kangnam.getId(), gyodae.getId(), 20, 10));
        Line secondLine = Line.of(2L, "2호선", "bg-green-500", LocalTime.of(06, 30),
            LocalTime.of(23, 00), 10);
        secondLine.addLineStation(LineStation.of(null, gyodae.getId(), 10, 10));
        secondLine.addLineStation(LineStation.of(gyodae.getId(), jamwon.getId(), 40, 30));
        secondLine.addLineStation(LineStation.of(jamwon.getId(), sinsa.getId(), 30, 10));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine, secondLine));
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(seolleung, yeoksam, kangnam, gyodae, jamwon, sinsa));
        graphs.initialize(lineRepository.findAll(), stationRepository.findAll());

        PathResponse pathResponse = pathService.findPath(2L, 6L, PathType.DISTANCE);
        assertThat(pathResponse.getStations()).size().isEqualTo(6);
        assertThat(pathResponse.getDistance()).isEqualTo(130);
        assertThat(pathResponse.getDuration()).isEqualTo(70);
    }

    @DisplayName("최단시간과 최단거리 기준으로 path를 구할 수 있다.")
    @Test
    void findPathBy() {
        Line first = Line.of(1L, "1호선", "bg-pink-600", LocalTime.of(10, 00), LocalTime.of(12, 00),
            1);
        Line second = Line.of(2L, "2호선", "bg-pink-600", LocalTime.of(10, 00), LocalTime.of(12, 00),
            1);
        Line third = Line.of(3L, "3호선", "bg-pink-600", LocalTime.of(10, 00), LocalTime.of(12, 00),
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
        graphs.initialize(lineRepository.findAll(), stationRepository.findAll());

        PathResponse durationResponse = pathService.findPath(1L, 5L, PathType.DURATION);
        PathResponse distanceResponse = pathService.findPath(1L, 5L, PathType.DISTANCE);
        assertThat(durationResponse.getStations()).size().isEqualTo(4);
        assertThat(durationResponse.getDuration()).isEqualTo(3);
        assertThat(durationResponse.getDistance()).isEqualTo(302);
        assertThat(distanceResponse.getStations()).size().isEqualTo(6);
        assertThat(distanceResponse.getDuration()).isEqualTo(5);
        assertThat(distanceResponse.getDistance()).isEqualTo(5);
    }

    @DisplayName("라인의 맨 앞에 새로운 구간을 추가하는 경우")
    @Test
    void addLineStationAtTheFirstOfLine() {

        firstLine.addLineStation(LineStation.of(null, kangnam.getId(), 10, 10));
        firstLine.addLineStation(LineStation.of(kangnam.getId(), seolleung.getId(), 10, 10));
        firstLine.addLineStation(LineStation.of(seolleung.getId(), yeoksam.getId(), 10, 10));
        when(lineRepository.findById(firstLine.getId())).thenReturn(Optional.of(firstLine));
        when(stationRepository.existsById(anyLong())).thenReturn(true);

        LineStationCreateRequest request = new LineStationCreateRequest(null, gyodae.getId(), 10,
            10);
        pathService.addLineStation(firstLine.getId(), request);

        assertThat(firstLine.getLineStations()).hasSize(4);

        List<Long> stationIds = firstLine.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(4L);
        assertThat(stationIds.get(1)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @DisplayName("라인의 중간에 새로운 구간을 추가하는 경우")
    @Test
    void addLineStationBetweenTwo() {
        addInitialLineStation();
        when(lineRepository.findById(firstLine.getId())).thenReturn(Optional.of(firstLine));
        when(stationRepository.existsById(anyLong())).thenReturn(true);

        LineStationCreateRequest request = new LineStationCreateRequest(1L,
            4L, 10, 10);
        pathService.addLineStation(firstLine.getId(), request);

        assertThat(firstLine.getLineStations()).hasSize(4);

        List<Long> stationIds = firstLine.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(4L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @DisplayName("라인의 마지막에 구간을 추가하는 경우")
    @Test
    void addLineStationAtTheEndOfLine() {
        addInitialLineStation();
        when(lineRepository.findById(firstLine.getId())).thenReturn(Optional.of(firstLine));
        when(stationRepository.existsById(anyLong())).thenReturn(true);

        LineStationCreateRequest request = new LineStationCreateRequest(3L,
            4L, 10, 10);
        pathService.addLineStation(firstLine.getId(), request);

        assertThat(firstLine.getLineStations()).hasSize(4);

        List<Long> stationIds = firstLine.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
    }

    @DisplayName("첫 역을 삭제하는 경우")
    @Test
    void removeLineStationAtTheFirstOfLine() {
        addInitialLineStation();
        when(lineRepository.findById(firstLine.getId())).thenReturn(Optional.of(firstLine));
        pathService.removeLineStation(firstLine.getId(), 1L);

        assertThat(firstLine.getLineStations()).hasSize(2);

        List<Long> stationIds = firstLine.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @DisplayName("중간역을 삭제하는 경우")
    @Test
    void removeLineStationBetweenTwo() {
        addInitialLineStation();
        when(lineRepository.findById(firstLine.getId())).thenReturn(Optional.of(firstLine));
        pathService.removeLineStation(firstLine.getId(), 2L);

        List<Long> stationIds = firstLine.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @DisplayName("노선의 마지막 역을 삭제하는 경우")
    @Test
    void removeLineStationAtTheEndOfLine() {
        addInitialLineStation();
        when(lineRepository.findById(firstLine.getId())).thenReturn(Optional.of(firstLine));
        pathService.removeLineStation(firstLine.getId(), 3L);

        assertThat(firstLine.getLineStations()).hasSize(2);

        List<Long> stationIds = firstLine.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }

    @DisplayName("노선의 구간을 가져오는 경우")
    @Test
    void findLineWithStationsById() {
        addInitialLineStation();
        List<Station> stations = Lists.newArrayList(Station.of("강남역"), Station.of("역삼역"),
            Station.of("삼성역"));
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(firstLine));
        when(stationRepository.findAllById(anyList())).thenReturn(stations);

        LineDetailResponse lineDetailResponse = pathService.findLineWithStationsById(1L);

        assertThat(lineDetailResponse.getStations()).hasSize(3);
    }

    @DisplayName("전체 노선도 가져오기")
    @Test
    void wholeLines() {
        addInitialLineStation();
        Line newLine = Line.of(2L, "신분당선", "bg-green-500", LocalTime.of(05, 30),
            LocalTime.of(22, 30), 5);
        newLine.addLineStation(LineStation.of(null, 4L, 10, 10));
        newLine.addLineStation(LineStation.of(4L, 5L, 10, 10));
        newLine.addLineStation(LineStation.of(5L, 6L, 10, 10));

        List<Station> stations = Arrays.asList(new Station(1L, "강남역", LocalDateTime.now()),
            new Station(2L, "역삼역", LocalDateTime.now()),
            new Station(3L, "삼성역", LocalDateTime.now()),
            new Station(4L, "양재역", LocalDateTime.now()),
            new Station(5L, "양재시민의숲역", LocalDateTime.now()),
            new Station(6L, "청계산입구역", LocalDateTime.now()));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine, newLine));
        when(stationRepository.findAll()).thenReturn(stations);

        List<LineDetailResponse> lineDetails = pathService.wholeLines().getLineDetailResponse();

        assertThat(lineDetails).isNotNull();
        assertThat(lineDetails.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetails.get(1).getStations().size()).isEqualTo(3);
    }

    private void addInitialLineStation() {
        firstLine.addLineStation(LineStation.of(null, 1L, 10, 10));
        firstLine.addLineStation(LineStation.of(1L, 2L, 10, 10));
        firstLine.addLineStation(LineStation.of(2L, 3L, 10, 10));
    }
}