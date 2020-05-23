package wooteco.subway.domain.path;

import java.util.List;

import org.jgrapht.GraphPath;

import wooteco.subway.domain.Station;

public class Path {
    private final GraphPath<Station, StationWeightEdge> path;

    public Path(GraphPath<Station, StationWeightEdge> path) {
        this.path = path;
    }

    public double duration() {
        return path.getEdgeList()
            .stream()
            .mapToDouble(StationWeightEdge::getDuration)
            .sum();
    }

    public double distance() {
        return path.getEdgeList()
            .stream()
            .mapToDouble(StationWeightEdge::getDistance)
            .sum();
    }

    public List<Station> getStationList() {
        return path.getVertexList();
    }
}
