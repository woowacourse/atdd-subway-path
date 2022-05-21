package wooteco.subway.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final Set<Long> passingLineIds;

    public Path(final List<Station> stations, final int distance, final HashSet<Long> passingLineIds) {
        this.stations = stations;
        this.distance = distance;
        this.passingLineIds = passingLineIds;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Long> getPassingLineIds() {
        return Collections.unmodifiableSet(passingLineIds);
    }
}
