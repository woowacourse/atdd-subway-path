package wooteco.subway.admin.dto;

import java.util.List;

public class SearchPathResponse {
    private int durationSum;
    private int distanceSum;
    private List<String> pathStationNames;

    public SearchPathResponse() {
    }

    public SearchPathResponse(int durationSum, int distanceSum, List<String> pathStationNames) {
        this.durationSum = durationSum;
        this.distanceSum = distanceSum;
        this.pathStationNames = pathStationNames;
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
