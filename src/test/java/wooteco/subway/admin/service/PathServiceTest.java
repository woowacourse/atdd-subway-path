package wooteco.subway.admin.service;

import org.assertj.core.util.Lists;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathSearchResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "잠실역";
    private static final String STATION_NAME6 = "석역";

    private PathService pathService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    private Line line1;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);
        station6 = new Station(6L, STATION_NAME6);

        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2 = new Line(2L, "8호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 10, 10));
        line1.addLineStation(new LineStation(1L, 2L, 10, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));
        line1.addLineStation(new LineStation(3L, 4L, 10, 10));
        line1.addLineStation(new LineStation(4L, 5L, 10, 10));
        line1.addLineStation(new LineStation(5L, 6L, 10, 10));

        line2.addLineStation(new LineStation(null, 2L, 11, 10));
        line2.addLineStation(new LineStation(2L, 4L, 11, 10));
        line2.addLineStation(new LineStation(4L, 6L, 11, 10));
    }

    @Test
    void searchPath() {
        List<Line> lines = Lists.newArrayList(line1, line2);
        List<Station> stations = Lists.newArrayList(station1, station2, station3, station4, station5, station6);

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findAll()).thenReturn(stations);

        PathSearchResponse pathSearchResponse = pathService.searchPath(STATION_NAME1, STATION_NAME6);

        assertThat(pathSearchResponse.getDistance()).isEqualTo(32);
        assertThat(pathSearchResponse.getDuration()).isEqualTo(30);
        assertThat(pathSearchResponse.getStations().size()).isEqualTo(4);
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

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }
}
