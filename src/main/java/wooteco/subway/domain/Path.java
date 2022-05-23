package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final int extraFare;

    public Path(List<Station> stations, int distance, int extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public int calculateFare() {
        return new Fare(distance).calculate(extraFare);
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }

    public int getDistance() {
        return distance;
    }
}
