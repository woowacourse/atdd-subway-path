package wooteco.subway.domain;

import java.util.List;

public class PathResult {
    private final int fare;
    private final List<Station> stations;
    private final int distance;

    public PathResult(List<Station> stations, double distance, Fare fare) {
        this.stations = stations;
        this.distance = (int) distance;
        this.fare = fare.getFare();
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
