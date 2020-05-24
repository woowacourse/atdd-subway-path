package wooteco.subway.admin.domain.line.path;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;

public class SubwayRoute {
    private final GraphPath<Long, RouteEdge> path;

    public SubwayRoute(GraphPath<Long, RouteEdge> path) {
        if (Objects.isNull(path)) {
            throw new NoPathException();
        }
        this.path = path;
    }

    public List<Long> getShortestPath() {
        return path.getVertexList();
    }

    public int calculateTotalDistance() {
        return path.getEdgeList()
            .stream()
            .mapToInt(RouteEdge::getDistance)
            .sum();
    }

    public int calculateTotalDuration() {
        return path.getEdgeList()
            .stream()
            .mapToInt(RouteEdge::getDuration)
            .sum();
    }
}
