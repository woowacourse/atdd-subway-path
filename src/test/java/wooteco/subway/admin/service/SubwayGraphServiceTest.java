package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.CustomEdge;
import wooteco.subway.admin.domain.LineStation;

class SubwayGraphServiceTest {
    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);

        graph.addVertex(1L);
        graph.addVertex(2L);
        graph.addVertex(3L);
        graph.addEdge(1L, 2L, new CustomEdge(new LineStation(1L, 2L, 2, 10), CriteriaType.DISTANCE));
        graph.addEdge(2L, 3L, new CustomEdge(new LineStation(2L, 3L, 2, 10), CriteriaType.DISTANCE));
        graph.addEdge(1L, 3L, new CustomEdge(new LineStation(1L, 3L, 100, 10), CriteriaType.DISTANCE));

        DijkstraShortestPath<Long, CustomEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(graph);
        List<Long> shortestPath
            = dijkstraShortestPath.getPath(3L, 1L).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }
}