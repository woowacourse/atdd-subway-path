package wooteco.subway.domain.path;

import java.util.List;
import java.util.Set;

public class Path {

    private final List<Long> stationIds;
    private final int distance;
    private final Set<Long> usedLineIds;

    public Path(List<Long> stationIds, int distance, Set<Long> usedLineIds) {
        this.stationIds = stationIds;
        this.distance = distance;
        this.usedLineIds = usedLineIds;
    }

    public Fare calculateFareByDistance(Fare extraFareByDistance) {
        return extraFareByDistance.addDefault(distance);
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Long> getUsedLineIds() {
        return usedLineIds;
    }
}
