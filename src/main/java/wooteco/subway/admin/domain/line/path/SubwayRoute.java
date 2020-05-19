package wooteco.subway.admin.domain.line.path;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;

public class SubwayRoute implements Path {
    private final GraphPath<Long, RouteEdge> path;

    public SubwayRoute(GraphPath<Long, RouteEdge> path) {
        if (Objects.isNull(path)) {
            throw new NoPathException();
        }
        this.path = path;
    }

    @Override
    public List<Long> getPath() {
        return path.getVertexList();
    }

    @Override
    public int calculateTotalDistance() {
        return path.getEdgeList()
            .stream()
            .mapToInt(RouteEdge::getDistance)
            .sum();
    }

    @Override
    public int calculateTotalDuration() {
        return path.getEdgeList()
            .stream()
            .mapToInt(RouteEdge::getDuration)
            .sum();
    }
}
