package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final double distance;
    private final Fare fare;

    public Path(final List<Station> stations, final double distance, final Fare fare) {
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

    public Fare getFare() {
        return fare;
    }
}
