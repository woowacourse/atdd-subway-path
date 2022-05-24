package wooteco.subway.domain;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final List<Long> includeLineIds;
    private final int distance;

    public Path(List<Station> stations, List<Long> includeLineIds, int distance) {
        this.stations = stations;
        this.includeLineIds = includeLineIds;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Long> getIncludeLineIds() {
        return includeLineIds;
    }

    public int getDistance() {
        return distance;
    }
}
