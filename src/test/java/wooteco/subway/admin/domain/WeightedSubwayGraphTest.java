package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WeightedSubwayGraphTest {
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

    @DisplayName("최단 거리를 generate하는 메서드 테스트")
    @Test
    public void getDijkstraShortestPath() {
        assertThat(weightedSubwayGraph.generateDijkstra()).isInstanceOf(DijkstraSubwayShortestPath.class);
    }
}