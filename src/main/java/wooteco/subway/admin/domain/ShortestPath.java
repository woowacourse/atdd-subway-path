package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.exception.VerticesNotConnectedException;

public class ShortestPath {
    private final GraphPath<Station, SubwayEdge> path;

    public ShortestPath(GraphPath<Station, SubwayEdge> path) {
        validateNull(path);
        this.path = path;
    }

    private void validateNull(GraphPath<Station, SubwayEdge> path) {
        if (Objects.isNull(path)) {
            throw new VerticesNotConnectedException();
        }
    }

    public List<Station> getVertices() {
        return path.getVertexList();
    }

    public Long calculateTotalDistance() {
        return path.getEdgeList()
                .stream()
                .mapToLong(SubwayEdge::getDistance)
                .sum();
    }

    public Long calculateTotalDuration() {
        return path.getEdgeList()
                .stream()
                .mapToLong(SubwayEdge::getDuration)
                .sum();
    }
}
