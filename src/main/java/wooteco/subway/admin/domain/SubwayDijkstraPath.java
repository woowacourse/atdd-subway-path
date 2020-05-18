package wooteco.subway.admin.domain;

import java.util.List;
import org.jgrapht.GraphPath;

public class SubwayDijkstraPath implements SubwayPath {
    private final GraphPath<Station, LineStationEdge> path;

    public SubwayDijkstraPath(GraphPath<Station, LineStationEdge> path) {
        this.path = path;
    }

    @Override
    public List<Station> stations() {
        return path.getVertexList();
    }

    @Override
    public int distance() {
        return path.getEdgeList().stream()
                .mapToInt(LineStationEdge::getDistance)
                .sum();
    }

    @Override
    public int duration() {
        return path.getEdgeList().stream()
                .mapToInt(LineStationEdge::getDuration)
                .sum();
    }
}
