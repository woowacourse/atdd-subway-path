package wooteco.subway.admin.dto;

import java.util.List;

public class GraphResponse {
    private final List<Long> path;
    private final int totalDistance;
    private final int totalDuration;

    public GraphResponse(List<Long> path, int totalDistance, int totalDuration) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
    }

    public List<Long> getPath() {
        return path;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
