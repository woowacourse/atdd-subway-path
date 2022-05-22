package wooteco.subway.domain;

import java.util.LinkedList;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final double fare;

    public Path(final List<Station> stations, final int distance, final double fare) {
        this.stations = new LinkedList<>(stations);
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return new LinkedList<>(stations);
    }

    public int getDistance() {
        return distance;
    }

    public double getFare() {
        return fare;
    }
}
