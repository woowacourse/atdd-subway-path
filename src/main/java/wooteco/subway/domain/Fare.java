package wooteco.subway.domain;

import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_SECOND = 2050;
    private static final int FARE_DISTANCE_LIMIT_FIRST = 10;
    private static final int FARE_DISTANCE_LIMIT_SECOND = 50;
    private static final int FARE_DISTANCE_UNIT_FIRST = 5;
    private static final int FARE_DISTANCE_UNIT_SECOND = 8;
    private static final int EXTRA_FARE = 100;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare from(int distance, int extraFare, int age) {
        int fare = calculateFare(distance) + extraFare;
        return getFare(age, fare);
    }

    private static Fare getFare(int age, int fare) {
        if (6 <= age && age < 13) {
            int discountFare = (int) ((fare - 350) * 0.5);
            return new Fare(fare - discountFare);
        }
        if (age < 19) {
            int discountFare = (int) ((fare - 350) * 0.2);
            return new Fare(fare - discountFare);
        }

        return new Fare(fare);
    }

    private static int calculateFare(int distance) {
        if (distance <= FARE_DISTANCE_LIMIT_FIRST) {
            return DEFAULT_FARE;
        }
        if (distance <= FARE_DISTANCE_LIMIT_SECOND) {
            return DEFAULT_FARE + (int) ((Math.ceil((distance - 11) / FARE_DISTANCE_UNIT_FIRST) + 1) * EXTRA_FARE);
        }
        return DEFAULT_FARE_SECOND + (int) ((Math.ceil((distance - 51) / FARE_DISTANCE_UNIT_SECOND) + 1) * EXTRA_FARE);
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
}
