package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final List<Long> stationIds;
    private final int distance;
    private final List<Long> lineIds;

    public Path(List<Long> stationIds, int distance, List<Long> lineIds) {
        this.stationIds = stationIds;
        this.distance = distance;
        this.lineIds = lineIds;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }

    public List<Long> getLineIds() {
        return lineIds;
    }
}
