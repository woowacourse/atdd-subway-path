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
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        seolleung = new Station(2L, "선릉역");
        yeoksam = new Station(3L, "역삼역");
        kangnam = new Station(1L, "강남역");
        gyodae = new Station(4L, "교대역");
        jamwon = new Station(5L, "잠원역");
        sinsa = new Station(6L, "신사역");
    }

    @DisplayName("source stationId와 target stationId를 받아서 최단 경로를 구한다.")
    @Test
    void findPathTest() {
        Line firstLine = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(23, 00), 10);
        firstLine.addLineStation(new LineStation(null, seolleung.getId(), 10, 10));
        firstLine.addLineStation(new LineStation(seolleung.getId(), yeoksam.getId(), 20, 10));
        firstLine.addLineStation(new LineStation(yeoksam.getId(), kangnam.getId(), 20, 10));
        firstLine.addLineStation(new LineStation(kangnam.getId(), gyodae.getId(), 20, 10));
        Line secondLine = new Line(2L, "2호선", LocalTime.of(06, 30), LocalTime.of(23, 00), 10);
        secondLine.addLineStation(new LineStation(null, gyodae.getId(), 10, 10));
        secondLine.addLineStation(new LineStation(gyodae.getId(), jamwon.getId(), 40, 30));
        secondLine.addLineStation(new LineStation(jamwon.getId(), sinsa.getId(), 30, 10));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine, secondLine));
        when(stationRepository.findAll()).thenReturn(
                Arrays.asList(seolleung, yeoksam, kangnam, gyodae, jamwon, sinsa));

        PathResponse pathResponse = pathService.findPath(2L, 6L);
        assertThat(pathResponse.getStations()).size().isEqualTo(6);
        assertThat(pathResponse.getDistance()).isEqualTo(130);
        assertThat(pathResponse.getDuration()).isEqualTo(70);
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
}