package wooteco.subway.admin.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest extends PathSetup {

    @BeforeEach
    void setup() {
        super.setup();
    }

    @DisplayName("PathType에 따라 Duration, Distance 제대로 찾는지 검증")
    @ParameterizedTest
    @CsvSource({"DISTANCE, 20, 60", "DURATION, 60, 20"})
    void calculateDuartionDistatnceByTypeTest(String pathType, int distance, int duration) {
        DijkstraShortestPath<Long, WeightedEdge> graph = new DijkstraShortestPath<>(PathGraph.getGraph(lineStations, PathType.of(pathType)));
        PathGraph pathGraph = new PathGraph(graph, PathType.of(pathType));

        Path path = pathGraph.createPath(1L, 3L);
        assertThat(path.getDistance()).isEqualTo(distance);
        assertThat(path.getDuration()).isEqualTo(duration);
    }

    @DisplayName("경로 확인")
    @Test
    void pathTest() {
        DijkstraShortestPath<Long, WeightedEdge> graph = new DijkstraShortestPath<>(PathGraph.getGraph(lineStations, PathType.DISTANCE));
        PathGraph pathGraph = new PathGraph(graph, PathType.DISTANCE);
        Path path = pathGraph.createPath(1L, 3L);

        List<Long> actual = path.getVertexList();
        List<Long> expected = Arrays.asList(1L, 2L, 3L);

        assertThat(actual).hasSize(3);
        assertThat(actual).containsAll(expected);
    }
}
