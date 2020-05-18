package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addEdge(new Edge(null, 1L, 10, 10));
        line.addEdge(new Edge(1L, 2L, 10, 10));
        line.addEdge(new Edge(2L, 3L, 10, 10));
    }

    @Test
    void addEdge() {
        line.addEdge(new Edge(null, 4L, 10, 10));

        assertThat(line.getEdges()).hasSize(4);
        Edge edge = line.getEdges().stream()
                .filter(it -> it.getStationId() == 1L)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        assertThat(edge.getPreStationId()).isEqualTo(4L);
    }

    @Test
    void getEdges() {
        List<Long> stationIds = line.getEdgesId();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeEdge(Long stationId) {
        line.removeEdgeById(stationId);

        assertThat(line.getEdges()).hasSize(2);
    }
}
