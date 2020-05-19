package wooteco.subway.admin.domain.line.path.vo;

public class PathInfo {
    private int totalDistance;
    private int totalDuration;

    public PathInfo(int totalDistance, int totalDuration) {
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
