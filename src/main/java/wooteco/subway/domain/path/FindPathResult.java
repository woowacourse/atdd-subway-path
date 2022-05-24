package wooteco.subway.domain.path;

import java.util.List;

public class FindPathResult {

    private final List<Long> stationIds;
    private final List<Long> usedLineIds;
    private final int totalDistance;

    public FindPathResult(final List<Long> stationIds, final List<Long> usedLineIds, final int totalDistance) {
        this.stationIds = stationIds;
        this.usedLineIds = usedLineIds;
        this.totalDistance = totalDistance;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public List<Long> getUsedLineIds() {
        return usedLineIds;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
