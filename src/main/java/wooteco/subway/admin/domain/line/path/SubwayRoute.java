package wooteco.subway.admin.domain.line.path;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.domain.line.path.vo.PathInfo;

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
    public PathInfo createPathInfo() {
        List<RouteEdge> edgeList = path.getEdgeList();
        int totalDistance = 0;
        int totalDuration = 0;
        for (RouteEdge routeEdge : edgeList) {
            totalDistance += routeEdge.getDistance();
            totalDuration += routeEdge.getDuration();
        }
        return new PathInfo(totalDistance, totalDuration);
    }
}
