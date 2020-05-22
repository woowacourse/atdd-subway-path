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
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.exception.NotExistPathException;
import wooteco.subway.admin.exception.StationNotFoundException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private PathService pathService;

    @Mock
    private LineService lineService;

    private GraphService graphService;

    private List<Line> lines;
    private List<Station> stations;

    @BeforeEach
    void setUp() {
        graphService = new GraphService();
        pathService = new PathService(lineService, graphService);

        stations = Arrays.asList(
                new Station(1L, "환-강남역"),
                new Station(2L, "1-역삼역"),
                new Station(3L, "환-삼성역"),
                new Station(4L, "1-삼송역"),
                new Station(5L, "환-지축역"),
                new Station(6L, "2-역삼역"),
                new Station(7L, "2-삼송역"),
                new Station(8L, "3-역삼역"),
                new Station(9L, "3-삼송역")
        );

        Line line1 = new Line(1L, "1호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        Line line2 = new Line(2L, "2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        Line line3 = new Line(3L, "3호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);

        line1.addLineStation(new LineStation(null, 1L, 10, 20));
        line1.addLineStation(new LineStation(1L, 2L, 10, 20));
        line1.addLineStation(new LineStation(2L, 3L, 10, 20));
        line1.addLineStation(new LineStation(3L, 4L, 10, 20));
        line1.addLineStation(new LineStation(4L, 5L, 10, 20));

        line2.addLineStation(new LineStation(null, 1L, 20, 10));
        line2.addLineStation(new LineStation(1L, 6L, 20, 10));
        line2.addLineStation(new LineStation(6L, 3L, 20, 11));
        line2.addLineStation(new LineStation(3L, 7L, 20, 10));
        line2.addLineStation(new LineStation(7L, 5L, 20, 10));

        line3.addLineStation(new LineStation(null, 1L, 50, 50));
        line3.addLineStation(new LineStation(1L, 8L, 50, 50));
        line3.addLineStation(new LineStation(8L, 3L, 50, 50));
        line3.addLineStation(new LineStation(3L, 9L, 50, 50));
        line3.addLineStation(new LineStation(9L, 5L, 50, 50));

        lines = Arrays.asList(line1, line2, line3);
        graphService.initialize(Stations.of(stations), Lines.of(lines));

    }

    @Test
    void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("최소 시간 구간 조회")
    @Test
    void shortestDuration() {
        when(lineService.findStationWithName(stations.get(0).getName())).thenReturn(stations.get(0));
        when(lineService.findStationWithName(stations.get(4).getName())).thenReturn(stations.get(4));

        PathInfo paths = pathService.retrieveShortestPath("환-강남역", "환-지축역", PathType.DURATION);
        assertThat(paths.getPath()).hasSize(5);
        assertThat(paths.getDistance()).isEqualTo(80);
        assertThat(paths.getDuration()).isEqualTo(41);
    }

    @DisplayName("최소 거리 구간 조회")
    @Test
    void shortestDistance() {
        when(lineService.findStationWithName(stations.get(0).getName())).thenReturn(stations.get(0));
        when(lineService.findStationWithName(stations.get(4).getName())).thenReturn(stations.get(4));

        PathInfo paths = pathService.retrieveShortestPath("환-강남역", "환-지축역", PathType.DISTANCE);
        assertThat(paths.getDistance()).isEqualTo(40);
    }

    @DisplayName("같은 역일 경우 빈 배열")
    @Test
    void sameSourceAndTarget() {
        when(lineService.findStationWithName(stations.get(0).getName())).thenReturn(stations.get(0));

        PathInfo paths = pathService.retrieveShortestPath("환-강남역", "환-강남역", PathType.DISTANCE);
        assertThat(paths.getPath()).hasSize(1);
        assertThat(paths.getPath().get(0).getName()).isEqualTo("환-강남역");
    }

    @DisplayName("연결되지 않은 역은 예외")
    @Test
    void notConnectedEdges() {
        List<Station> stations = new ArrayList<>(this.stations);
        stations.add(new Station(10L, "4-충무로역"));
        stations.add(new Station(11L, "4-오이도역"));

        Line line4 = new Line(4L, "4호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line4.addLineStation(new LineStation(null, 10L, 10, 10));
        line4.addLineStation(new LineStation(10L, 11L, 10, 10));
        List<Line> lines = new ArrayList<>(this.lines);
        lines.add(line4);
        graphService.initialize(Stations.of(stations), Lines.of(lines));

        when(lineService.findStationWithName(stations.get(0).getName())).thenReturn(stations.get(0));
        when(lineService.findStationWithName(stations.get(10).getName())).thenReturn(stations.get(10));

        assertThatThrownBy(() -> {
            pathService.retrieveShortestPath("환-강남역", "4-오이도역", PathType.DISTANCE);
        }).isInstanceOf(NotExistPathException.class)
                .hasMessage("(환-강남역, 4-오이도역) 구간이 존재하지 않습니다.");
    }

    @DisplayName("존재하지 않는 역은 예외")
    @Test
    void notExistSourceOrTarget() {
        String invalidStationName = "없는 역";
        when(lineService.findStationWithName(stations.get(0).getName())).thenReturn(stations.get(0));
        when(lineService.findStationWithName(invalidStationName))
                .thenThrow(new StationNotFoundException(invalidStationName));

        assertThatThrownBy(() -> {
            pathService.retrieveShortestPath("환-강남역", invalidStationName, PathType.DISTANCE);
        }).isInstanceOf(StationNotFoundException.class)
                .hasMessage("이름이 없는 역인 역이 존재하지 않습니다.");
    }
}