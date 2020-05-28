package wooteco.subway.admin.domain.path;

import java.util.List;

public class SubwayPath {
    private List<Long> path;
    private List<LineStationEdge> weight;

    public SubwayPath(List<Long> path, List<LineStationEdge> weight) {
        this.path = path;
        this.weight = weight;
    }

    public List<Long> getVertexList() {
        return path;
    }

    public int totalDuration() {
        return weight.stream()
            .mapToInt(LineStationEdge::getDuration)
            .sum();
    }

    public int totalDistance() {
        return weight.stream()
            .mapToInt(LineStationEdge::getDistance)
            .sum();
    }
}
