package wooteco.subway.admin.dto;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int totalDistance;
    private int totalDuration;

    public PathResponse(List<StationResponse> stations, int totalDistance,
        int totalDuration) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
