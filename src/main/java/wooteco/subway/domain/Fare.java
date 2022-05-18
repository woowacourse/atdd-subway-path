package wooteco.subway.domain;

import java.util.Objects;

public class Fare {

    public static final int DEFAULT_FARE = 1250;
    public static final int OVER_FARE_AT_50 = 800;
    public static final int OVER_FARE_AMOUNT_PER_UNIT_DISTANCE = 100;
    public static final int NO_OVER_FARE_DISTANCE = 10;
    public static final int FIRST_OVER_FARE_DISTANCE = 50;
    public static final int FIRST_OVER_FARE_UNIT_DISTANCE = 5;
    public static final int SECOND_OVER_FARE_UNIT_DISTANCE = 8;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int distance) {
        return new Fare(DEFAULT_FARE + calculateOverFare(distance));
    }

    private static int calculateOverFare(int distance) {
        if (distance <= NO_OVER_FARE_DISTANCE) {
            return 0;
        }

        if (distance <= FIRST_OVER_FARE_DISTANCE) {
            return calculateOverFareWithDistanceUnit(distance - NO_OVER_FARE_DISTANCE, FIRST_OVER_FARE_UNIT_DISTANCE);
        }

        return OVER_FARE_AT_50 + calculateOverFareWithDistanceUnit(distance - FIRST_OVER_FARE_DISTANCE,
                SECOND_OVER_FARE_UNIT_DISTANCE);
    }

    private static int calculateOverFareWithDistanceUnit(int distance, int unitDistance) {
        int numberOfImposition = (distance - 1) / unitDistance + 1;
        return numberOfImposition * OVER_FARE_AMOUNT_PER_UNIT_DISTANCE;
    }

    public int getValue() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + fare +
                '}';
    }
}
