package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EdgesTest {
    private Edges edges;

    @BeforeEach
    void setUp() {
        Edge edge1 = new Edge(null, 1L, 10, 10);
        Edge edge2 = new Edge(1L, 2L, 10, 10);
        Edge edge3 = new Edge(2L, 3L, 10, 10);
        edges = new Edges();
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
    }

    @DisplayName("edge를 추가했을때 개수가 증가하는지 테스트")
    @Test
    void addTest() {
        edges.add(new Edge(null, 4L, 10, 10));

        assertThat(edges.getEdges()).hasSize(4);
        Edge edge = edges.getEdges().stream()
                .filter(it -> it.getStationId() == 1L)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        assertThat(edge.getPreStationId()).isEqualTo(4L);
    }

    @DisplayName("역id로 edge를 삭제했을때 개수가 감소하는지 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeTest(Long stationId) {
        edges.removeByStationId(stationId);

        assertThat(edges.getEdges()).hasSize(2);
    }

    @DisplayName("역의 id가 순서대로 구해지는지 테스트")
    @Test
    void getSortedStationIdsTest() {
        List<Long> stationIds = edges.getSortedStationIds();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }

}
