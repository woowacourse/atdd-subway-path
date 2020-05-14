package wooteco.subway.admin.domain;

import java.util.List;

import org.jgrapht.GraphPath;

public class ShortestPath {
    private final GraphPath<Station, SubwayEdge> path;

    public ShortestPath(GraphPath<Station, SubwayEdge> path) {
        this.path = path;
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
