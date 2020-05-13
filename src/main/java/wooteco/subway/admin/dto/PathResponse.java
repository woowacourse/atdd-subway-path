package wooteco.subway.admin.dto;

import java.util.List;
import java.util.Objects;

public class PathResponse {
    private List<StationResponse> path;
    private int totalDuration;
    private int totalDistance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> path, int totalDuration, int totalDistance) {
        this.path = path;
        this.totalDuration = totalDuration;
        this.totalDistance = totalDistance;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathResponse that = (PathResponse) o;
        return totalDuration == that.totalDuration &&
                totalDistance == that.totalDistance &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, totalDuration, totalDistance);
    }
}
