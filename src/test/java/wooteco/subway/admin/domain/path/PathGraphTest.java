package wooteco.subway.admin.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.CustomException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathGraphTest extends PathSetup {
    @DisplayName("전체 호선에 대한 그래프 생성 확인")
    @Test
    void createDistanceGraph() {
        DijkstraShortestPath<Long, WeightedEdge> graph = new DijkstraShortestPath<>(PathGraph.getGraph(lineStations, PathType.DURATION));
        PathGraph pathGraph = new PathGraph(graph, PathType.DURATION);

        assertThat(pathGraph.getTotalPath()).isInstanceOf(DijkstraShortestPath.class);
    }

    @DisplayName("(예외) Vertex는 있지만, 연결되지 않은 경로")
    @Test
    void notConnectedPathTest() {
        DijkstraShortestPath<Long, WeightedEdge> graph = new DijkstraShortestPath<>(PathGraph.getGraph(lineStations, PathType.DISTANCE));
        ;
        PathGraph pathGraph = new PathGraph(graph, PathType.DISTANCE);
        assertThatThrownBy(() -> pathGraph.createPath(1L, 9L))
                .isInstanceOf(CustomException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }

    @DisplayName("(예외) Vertex가 없어 존재하지 않는 경로")
    @Test
    void notExistVertexPathTest() {
        DijkstraShortestPath<Long, WeightedEdge> graph = new DijkstraShortestPath<>(PathGraph.getGraph(lineStations, PathType.DISTANCE));
        PathGraph pathGraph = new PathGraph(graph, PathType.DISTANCE);
        assertThatThrownBy(() -> pathGraph.createPath(100L, 123L))
                .isInstanceOf(CustomException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }
}
