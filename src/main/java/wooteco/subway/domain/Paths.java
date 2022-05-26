package wooteco.subway.domain;

import java.util.List;

public class Paths {

    private final List<Station> stations;
    private final List<Long> lineIds;
    private final double distance;

    public Paths(final List<Station> stations, final List<Long> lineIds, final double distance) {
        this.stations = stations;
        this.lineIds = lineIds;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public List<Long> getLineIds() {
        return lineIds;
    }
}
