package wooteco.subway.domain.path;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import wooteco.subway.domain.path.strategy.PathFindingStrategy;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Path {
    private final GraphPath<Long, PathEdge> graphPath;

    public Path(GraphPath<Long, PathEdge> graphPath) {
        this.graphPath = graphPath;
    }

    public static Path of(Graph<Long, PathEdge> graph, Long source, Long target, PathFindingStrategy pathFindingStrategy) {
        GraphPath<Long, PathEdge> shortestPath = pathFindingStrategy.findPathBetween(graph, source, target);
        return new Path(shortestPath);
    }

    public List<Long> getNodes() {
        return graphPath.getVertexList();
    }

    public int calculateDistance() {
        return (int) graphPath.getEdgeList()
                .stream()
                .mapToDouble(PathEdge::getWeight)
                .sum();
    }

    public int getHighestExtraFare(Map<Long, Integer> lineExtraFares) {
        return graphPath.getEdgeList()
                .stream()
                .map(PathEdge::getLineId)
                .mapToInt(lineExtraFares::get)
                .max()
                .orElseThrow(() -> new NoSuchElementException("경로가 존재하지 않습니다."));
    }
}
