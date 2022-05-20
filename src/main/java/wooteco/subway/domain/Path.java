package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Path {

    private static final int BASE_FARE = 1250;
    private static final int FIRST_DEFAULT_FARE = 800;
    private static final int FIRST_SURCHARGE_DISTANCE = 10;
    private static final int SECOND_SURCHARGE_DISTANCE = 50;
    private static final int FIRST_SURCHARGE_DIVIDE_DISTANCE = 5;
    private static final int SECOND_SURCHARGE_DIVIDE_DISTANCE = 8;

    private final List<Station> stations;
    private final int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int calculateFare() {
        if (distance <= FIRST_SURCHARGE_DISTANCE) {
            return BASE_FARE;
        }
        if (distance <= SECOND_SURCHARGE_DISTANCE) {
            return BASE_FARE + calculateOverFare(distance - FIRST_SURCHARGE_DISTANCE,
                FIRST_SURCHARGE_DIVIDE_DISTANCE);
        }
        return BASE_FARE + FIRST_DEFAULT_FARE + calculateOverFare(
            distance - SECOND_SURCHARGE_DISTANCE,
            SECOND_SURCHARGE_DIVIDE_DISTANCE);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return getDistance() == path.getDistance() && Objects.equals(getStations(),
            path.getStations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStations(), getDistance());
    }
}
