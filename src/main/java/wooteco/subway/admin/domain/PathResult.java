package wooteco.subway.admin.domain;

import java.util.List;

public class PathResult {
    private final List<Long> path;
    private final int totalDistance;
    private final int totalDuration;

    public PathResult(List<Long> path, int totalDistance, int totalDuration) {
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
