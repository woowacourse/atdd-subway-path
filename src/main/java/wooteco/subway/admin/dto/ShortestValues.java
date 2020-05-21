package wooteco.subway.admin.dto;

import java.util.List;

public class ShortestValues {
    private int distanceSum;
    private int durationSum;
    List<String> stationNames;

    public ShortestValues(int distanceSum, int durationSum, List<String> stationNames) {
        this.distanceSum = distanceSum;
        this.durationSum = durationSum;
        this.stationNames = stationNames;
    }

    public int getDistanceSum() {
        return distanceSum;
    }

    public int getDurationSum() {
        return durationSum;
    }

    public List<String> getStationNames() {
        return stationNames;
    }
}
