package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int calculateFare() {
        if (distance <= 10) {
            return 1250;
        }
        if (distance <= 50) {
            return 1250 + calculateOverFare(distance - 10, 5);
        }
        return 1250 + 800 + calculateOverFare(distance - 50, 8);
    }

    private int calculateOverFare(int distance, int divideDistance) {
        return (int) ((Math.ceil((distance - 1) / divideDistance) + 1) * 100);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
