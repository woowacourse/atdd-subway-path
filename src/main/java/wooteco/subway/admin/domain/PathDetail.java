package wooteco.subway.admin.domain;

import java.util.List;

public class PathDetail {
    private final List<Long> paths;
    private final Integer totalDistance;
    private final Integer totalDuration;

    public PathDetail(final List<Long> paths, final Integer totalDistance, final Integer totalDuration) {
        this.paths = paths;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
    }

    public List<Long> getPaths() {
        return paths;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }
}
