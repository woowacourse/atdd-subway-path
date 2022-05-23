package wooteco.subway.domain;

import java.util.List;

public class Paths {

    private final List<Station> stations;
    private final double distance;
    private final int fare;

    public Paths(final List<Station> stations, final double distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
