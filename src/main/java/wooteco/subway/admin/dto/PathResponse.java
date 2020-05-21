package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stationResponses;
    private final int totalDistance;
    private final int totalDuration;

    public PathResponse(List<StationResponse> stationResponses, int totalDistance,
        int totalDuration) {
        this.stationResponses = stationResponses;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
