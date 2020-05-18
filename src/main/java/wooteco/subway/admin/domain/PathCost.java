package wooteco.subway.admin.domain;

public class PathCost {
    private final Integer distance;
    private final Integer duration;

    public PathCost(final Integer distance, final Integer duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}
