package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;

import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
class PathAlgorithmByDijkstraTest {

    private Line line1;
    private Line line2;

    private WeightedMultigraph<Long, LineStationEdge> graphByDistance;
    private WeightedMultigraph<Long, LineStationEdge> graphByDuration;

    private PathAlgorithm pathAlgorithm;

    @BeforeEach
    void setUp() {
        line1 = Line.of(1L, "2호선", "bg-green-400", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 0, 0));
        line1.addLineStation(new LineStation(1L, 2L, 5, 10));
        line1.addLineStation(new LineStation(2L, 3L, 5, 10));
        line1.addLineStation(new LineStation(3L, 6L, 5, 10));

        line2 = Line.of(2L, "분당선", "bg-yellow-500", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 1L, 0, 0));
        line2.addLineStation(new LineStation(1L, 4L, 10, 5));
        line2.addLineStation(new LineStation(4L, 5L, 10, 5));
        line2.addLineStation(new LineStation(5L, 6L, 10, 5));

        graphByDistance = Graph.of(Arrays.asList(line1, line2), PathType.DISTANCE).getGraph();
        graphByDuration = Graph.of(Arrays.asList(line1, line2), PathType.DURATION).getGraph();

        pathAlgorithm = new PathAlgorithmByDijkstra();
    }

    @Test
    @DisplayName("최단 거리 경로를 구한다")
    void getShortestPathByDistance() {
        PathResult path = pathAlgorithm.findPath(1L, 6L, Graph.of(graphByDistance));
        assertThat(path.getPath()).containsExactly(1L, 2L, 3L, 6L);
        assertThat(path.getTotalDistance()).isEqualTo(15);
        assertThat(path.getTotalDuration()).isEqualTo(30);
    }

    @Test
    @DisplayName("최소 시간 경로를 구한다")
    void getShortestPathByDuration() {
        PathResult path = pathAlgorithm.findPath(1L, 6L, Graph.of(graphByDuration));
        assertThat(path.getPath()).containsExactly(1L, 4L, 5L, 6L);
        assertThat(path.getTotalDistance()).isEqualTo(30);
        assertThat(path.getTotalDuration()).isEqualTo(15);
    }
}