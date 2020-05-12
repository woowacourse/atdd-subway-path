package wooteco.subway.admin.dto;

import java.util.List;

public class SearchPathResponse {
    private int totalDuration;
    private int totalDistance;
    private List<String> stationNames;

    public SearchPathResponse(int totalDuration, int totalDistance, List<String> stationNames) {
        this.totalDuration = totalDuration;
        this.totalDistance = totalDistance;
        this.stationNames = stationNames;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<String> getStationNames() {
        return stationNames;
    }
}
