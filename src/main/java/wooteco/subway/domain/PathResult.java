package wooteco.subway.domain;

import java.util.List;

public class PathResult {
    private final int fare;
    private final List<Station> stations;
    private final int distance;

    public PathResult(List<Station> stations, double distance, int extraFare) {
        this.stations = stations;
        this.distance = (int) distance;
        this.fare = new Fare(distance, extraFare).getFare();
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }
}
