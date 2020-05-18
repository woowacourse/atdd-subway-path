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
class GraphTest {

    private Line line1;
    private Line line2;

    private WeightedMultigraph<Long, LineStationEdge> graphByDistance;
    private WeightedMultigraph<Long, LineStationEdge> graphByDuration;

    @BeforeEach
    void setUp() {
        line1 = new Line(1L, "2호선", "bg-green-400", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 5, 10));
        line1.addLineStation(new LineStation(1L, 2L, 5, 10));
        line1.addLineStation(new LineStation(2L, 3L, 5, 10));

        line2 = new Line(2L, "분당선", "bg-yellow-500", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 4L, 10, 5));
        line2.addLineStation(new LineStation(4L, 2L, 10, 5));
        line2.addLineStation(new LineStation(2L, 5L, 10, 5));

        graphByDistance = Graph.of(Arrays.asList(line1, line2), PathType.DISTANCE).getGraph();
        graphByDuration = Graph.of(Arrays.asList(line1, line2), PathType.DURATION).getGraph();
    }

    @Test
    @DisplayName("그래프에 vertex가 존재한다")
    void vertex() {
        assertThat(graphByDistance.vertexSet()).containsExactly(1L, 2L, 3L, 4L, 5L);
        assertThat(graphByDuration.vertexSet()).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    @DisplayName("그래프에 Edge가 존재한다")
    void edge() {
        assertThat(graphByDistance.containsEdge(1L, 2L)).isTrue();
        assertThat(graphByDistance.containsEdge(2L, 3L)).isTrue();
        assertThat(graphByDistance.containsEdge(4L, 2L)).isTrue();
        assertThat(graphByDistance.containsEdge(2L, 5L)).isTrue();

        assertThat(graphByDuration.containsEdge(1L, 2L)).isTrue();
        assertThat(graphByDuration.containsEdge(2L, 3L)).isTrue();
        assertThat(graphByDuration.containsEdge(4L, 2L)).isTrue();
        assertThat(graphByDuration.containsEdge(2L, 5L)).isTrue();
    }

    @Test
    void weight() {
        assertThat(graphByDistance.getEdge(1L, 2L).getWeight()).isEqualTo(5);
        assertThat(graphByDistance.getEdge(2L, 3L).getWeight()).isEqualTo(5);
        assertThat(graphByDistance.getEdge(4L, 2L).getWeight()).isEqualTo(10);
        assertThat(graphByDistance.getEdge(2L, 5L).getWeight()).isEqualTo(10);

        assertThat(graphByDuration.getEdge(1L, 2L).getWeight()).isEqualTo(10);
        assertThat(graphByDuration.getEdge(2L, 3L).getWeight()).isEqualTo(10);
        assertThat(graphByDuration.getEdge(4L, 2L).getWeight()).isEqualTo(5);
        assertThat(graphByDuration.getEdge(2L, 5L).getWeight()).isEqualTo(5);
    }
}