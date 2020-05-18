package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
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
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

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
        Line secondLine = new Line(2L, "2호선", "bg-green-500", LocalTime.of(6, 30),
                LocalTime.of(23, 00), 10);
        secondLine.addLineStation(LineStation.of(null, gyodae.getId(), 10, 10));
        secondLine.addLineStation(LineStation.of(gyodae.getId(), jamwon.getId(), 40, 30));
        secondLine.addLineStation(LineStation.of(jamwon.getId(), sinsa.getId(), 30, 10));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine, secondLine));
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(seolleung, yeoksam, kangnam, gyodae, jamwon, sinsa));

        PathResponse pathResponse = pathService.findPath(2L, 6L, PathType.DISTANCE);
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

        PathResponse durationResponse = pathService.findPath(1L, 5L, PathType.DURATION);
        PathResponse distanceResponse = pathService.findPath(1L, 5L, PathType.DISTANCE);
        assertThat(durationResponse.getStations()).size().isEqualTo(4);
        assertThat(durationResponse.getDuration()).isEqualTo(3);
        assertThat(durationResponse.getDistance()).isEqualTo(302);
        assertThat(distanceResponse.getStations()).size().isEqualTo(6);
        assertThat(distanceResponse.getDuration()).isEqualTo(5);
        assertThat(distanceResponse.getDistance()).isEqualTo(5);
    }

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("등록되지 않은 경우")
    @Test
    public void dijkstraShortestPathNotExistException() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        assertThatThrownBy(() -> dijkstraShortestPath.getPath("v3", "v1").getVertexList())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("연결되지 않은 경우")
    @Test
    public void dijkstraShortestPathNotConnectedException() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        assertThatThrownBy(() -> dijkstraShortestPath.getPath("v3", "v1").getVertexList())
            .isInstanceOf(NullPointerException.class);
    }
}