package wooteco.subway.domain.path;

import java.util.List;

public class PathDecision {

    private final List<Long> stationIds;
    private final double distance;

    public PathDecision(final List<Long> stationIds, final double distance) {
        this.stationIds = stationIds;
        this.distance = distance;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public double getDistance() {
        return distance;
    }
}
