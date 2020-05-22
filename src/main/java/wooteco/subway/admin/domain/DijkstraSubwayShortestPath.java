package wooteco.subway.admin.domain;

import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraSubwayShortestPath {
    private DijkstraShortestPath<Long, CustomEdge> dijkstraShortestPath;

    public DijkstraSubwayShortestPath(WeightedMultigraph<Long, CustomEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public GraphPath<Long, CustomEdge> getShortestPath(Long source, Long target) {
        final GraphPath<Long, CustomEdge> shortestPathPath = dijkstraShortestPath.getPath(source, target);

        validateNoConnection(shortestPathPath);

        return shortestPathPath;
    }

    private void validateNoConnection(GraphPath<Long, CustomEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }
}
