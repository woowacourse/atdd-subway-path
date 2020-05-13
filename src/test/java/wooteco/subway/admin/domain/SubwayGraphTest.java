package wooteco.subway.admin.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayGraphTest {

    @DisplayName("최단 거리 경로에 있는 모든 역의 아이디를 순서대로 반환한다.")
    @Test
    void getShortestPath() {
        //given
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(11, 30), 10);
        line.addEdge(new Edge(4L, 1L, 100, 10));
        line.addEdge(new Edge(1L, 2L, 10, 10));
        line.addEdge(new Edge(2L, 3L, 11, 10));
        line.addEdge(new Edge(3L, 4L, 12, 10));
        SubwayGraph subwayGraph = new SubwayGraph(line.getEdges());

        //when
        List<Long> shortestPath = subwayGraph.getShortestPath(1L, 4L);

        //then
        assertThat(shortestPath).containsExactly(1L, 2L, 3L, 4L);
    }

    @DisplayName("찾은 최단 거리의 값을 구한다.")
    @Test
    void sumOfEdgeWeights() {
        //given
        Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(11, 30), 10);
        line.addEdge(new Edge(4L, 1L, 100, 10));
        line.addEdge(new Edge(1L, 2L, 10, 10));
        line.addEdge(new Edge(2L, 3L, 11, 10));
        line.addEdge(new Edge(3L, 4L, 12, 10));
        SubwayGraph subwayGraph = new SubwayGraph(line.getEdges());

        //when
        List<Long> shortestPath = subwayGraph.getShortestPath(1L, 4L);
        double totalDistance = subwayGraph.sumOfEdgeWeights(shortestPath);

        //then
        assertThat(totalDistance).isEqualTo(33);
    }
}