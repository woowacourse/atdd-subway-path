package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.exception.VerticesNotConnectedException;

public class ShortestPath {
    private final GraphPath<Station, Edge> path;

    public ShortestPath(GraphPath<Station, Edge> path) {
        validateNull(path);
        this.path = path;
    }

    private void validateNull(GraphPath<Station, Edge> path) {
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
                .mapToLong(Edge::getDistance)
                .sum();
    }

    public Long calculateTotalDuration() {
        return path.getEdgeList()
                .stream()
                .mapToLong(Edge::getDuration)
                .sum();
    }
}
