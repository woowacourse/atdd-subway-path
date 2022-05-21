package wooteco.subway.domain.path.fare;

import java.util.Objects;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private final int value;

    Fare(int value) {
        this.value = value;
    }

    public static Fare of(int distance) {
        return new Fare(calculateFare(distance));
    }

    private static int calculateFare(int distance) {
        DistanceOverFare distanceOverFare = DistanceOverFare.of(distance);
        return BASIC_FARE + distanceOverFare.getValue();
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" + "value=" + value + '}';
    }
}
