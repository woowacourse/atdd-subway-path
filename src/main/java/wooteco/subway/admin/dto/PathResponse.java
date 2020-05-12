package wooteco.subway.admin.dto;

import java.util.List;

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
}
