package wooteco.subway.domain;

import java.util.List;

public class Path {

    private static final int DEFAULT_FARE = 1250;

    private final List<Station> stations;
    private final int distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int calculateFare() {
        if (distance <= 10) {
            return DEFAULT_FARE;
        }
        if (distance <= 50) {
            return DEFAULT_FARE + calculateOverFare(distance - 10, 5, 100);
        }
        return 0;
    }

    private int calculateOverFare(int distance, int unitDistance, int overFare) {
        return (int) ((Math.ceil((distance - 1) / unitDistance) + 1) * overFare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
