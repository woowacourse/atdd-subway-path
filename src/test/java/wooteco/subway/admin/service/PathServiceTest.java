package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);
    }

    @DisplayName("경로 조회 테스트")
    @Test
    void getPath() {
        Line line1 = new Line(1L, "1호선", LocalTime.of(5, 30), LocalTime.of(5, 30), 10, "bg-teal-500");
        Line line2 = new Line(2L, "2호선", LocalTime.of(5, 30), LocalTime.of(5, 30), 10, "bg-teal-300");

        Station station1 = new Station(1L, "1역");
        Station station2 = new Station(2L, "2역");
        Station station3 = new Station(3L, "3역");
        Station station4 = new Station(4L, "4역");
        Station station5 = new Station(5L, "5역");

        line1.addLineStation(new LineStation(null, station1.getId(), 0, 0));
        line1.addLineStation(new LineStation(station1.getId(), station2.getId(), 10, 2));
        line1.addLineStation(new LineStation(station2.getId(), station3.getId(), 10, 0));
        line2.addLineStation(new LineStation(null, station2.getId(), 0, 0));
        line2.addLineStation(new LineStation(station2.getId(), station4.getId(), 2, 0));
        line2.addLineStation(new LineStation(station4.getId(), station5.getId(), 2, 2));
        line2.addLineStation(new LineStation(station5.getId(), station3.getId(), 2, 0));

        when(stationRepository.findByName("1역")).thenReturn(Optional.of(station1));
        when(stationRepository.findByName("3역")).thenReturn(Optional.of(station3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));

        PathResponse path = pathService.getPath("1역", "3역");
        assertThat(path.getStations()).hasSize(5);
        assertThat(path.getDistance()).isEqualTo(16);
        assertThat(path.getDuration()).isEqualTo(4);
    }

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
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
