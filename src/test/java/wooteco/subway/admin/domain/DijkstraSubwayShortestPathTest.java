package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;

import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DijkstraSubwayShortestPathTest {
    private WeightedSubwayGraph weightedSubwayGraph;

    @BeforeEach
    void setUp() {
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        LineStation lineStation1 = new LineStation(null, 1L, 10, 10);
        LineStation lineStation2 = new LineStation(1L, 2L, 10, 10);
        LineStation lineStation3 = new LineStation(2L, 3L, 10, 10);

        line.addLineStation(lineStation1);
        line.addLineStation(lineStation2);
        line.addLineStation(lineStation3);

        weightedSubwayGraph = new WeightedSubwayGraph(Arrays.asList(line), CriteriaType.DISTANCE);
    }

    @DisplayName("최단거리를 그래프를 인스턴스로 만드는 테스트")
    @Test
    void getShortestPathInstanceGraphPath() {
        DijkstraSubwayShortestPath dijkstraSubwayShortestPath = weightedSubwayGraph.generateDijkstra();

        assertThat(dijkstraSubwayShortestPath.getShortestPath(3L, 1L)).isInstanceOf(GraphPath.class);
    }

    @DisplayName("최단거리를 그래프를 만드는 테스트")
    @Test
    void getShortestPath() {
        DijkstraSubwayShortestPath dijkstraSubwayShortestPath = weightedSubwayGraph.generateDijkstra();
        GraphPath<Long, CustomEdge> graphPath = dijkstraSubwayShortestPath.getShortestPath(3L, 1L);

        assertThat(graphPath.getVertexList()).hasSize(3);
    }
}