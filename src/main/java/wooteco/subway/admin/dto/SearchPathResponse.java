package wooteco.subway.admin.dto;

import java.util.List;

public class SearchPathResponse {
    private int durationSum;
    private int distanceSum;
    private List<String> pathStationNames;

    public SearchPathResponse() {
    }

    public SearchPathResponse(ShortestValues shortestValues) {
        this.durationSum = shortestValues.getDurationSum();
        this.distanceSum = shortestValues.getDistanceSum();
        this.pathStationNames = shortestValues.getStationNames();
    }

    public int getDurationSum() {
        return durationSum;
    }

    public int getDistanceSum() {
        return distanceSum;
    }

    public List<String> getPathStationNames() {
        return pathStationNames;
    }
}
