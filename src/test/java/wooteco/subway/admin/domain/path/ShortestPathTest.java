package wooteco.subway.admin.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.vo.Edges;

public class ShortestPathTest {

    private Line line1;
    private Line line2;

    @BeforeEach
    void setUp() {
        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new Edge(null, 1L, 10, 10));
        line1.addLineStation(new Edge(1L, 2L, 10, 10));
        line1.addLineStation(new Edge(2L, 3L, 10, 10));

        line2 = new Line(2L, "8호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new Edge(null, 5L, 10, 10));
        line2.addLineStation(new Edge(5L, 2L, 10, 10));
        line2.addLineStation(new Edge(2L, 6L, 10, 10));
    }

    @DisplayName("전체 호선에 대한 그래프 생성 확인")
    @Test
    void createDistanceGraph() {
        List<Edge> edges = Stream.of(line1, line2)
            .map(Line::getEdges)
            .map(Edges::getEdges)
            .flatMap(List::stream)
            .collect(Collectors.toList());
        ShortestPath shortestPath = ShortestPath.of(edges, PathType.DURATION);
        assertThat(shortestPath.getPath()).isInstanceOf(DijkstraShortestPath.class);
    }
}
