package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    @DisplayName("모든 구간을 파라미터로 받아서 최단 경로 생성")
    void create() {
        List<Section> sections = List.of(
                Section.createOf(1L, 1L, 1L, 2L, 5, 1L),
                Section.createOf(2L, 1L, 2L, 3L, 5, 2L)
        );

        Path path = Path.from(new Sections(sections), 1L, 3L);

        assertThat(path.getTotalDistance()).isEqualTo(10);
    }
}
