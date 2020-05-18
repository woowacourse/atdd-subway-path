package wooteco.subway.admin.domain;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.graph.PathCost;
import wooteco.subway.admin.domain.graph.SubwayPath;
import wooteco.subway.admin.domain.graph.jgraph.SubwayJGraph;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayJGraphTest {

    @DisplayName("최단 경로를 찾는다")
    @Test
    void getShortestPath() {
        //given
        Line line1 = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(11, 30), 10);
        line1.addEdge(new Edge(null, 1L, 1, 1));
        line1.addEdge(new Edge(1L, 2L, 1, 1));
        line1.addEdge(new Edge(2L, 3L, 10, 1));

        Line line2 = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(11, 30), 10);
        line2.addEdge(new Edge(2L, 3L, 1, 10));
        SubwayJGraph subwayJGraph = new SubwayJGraph(Sets.union(line1.getEdges(), line2.getEdges()), Edge::getDistance);

        //when
        SubwayPath path = subwayJGraph.getPath(1L, 3L);
        PathCost cost = path.getCost();
        Integer totalDistance = cost.getDistance();
        Integer totalDuration = cost.getDuration();

        //then
        assertThat(path.getPaths()).containsExactly(1L, 2L, 3L);
        assertThat(totalDistance).isEqualTo(2);
        assertThat(totalDuration).isEqualTo(11);
    }
}