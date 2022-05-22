package wooteco.subway.domain;

import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_SECOND = 2050;
    private static final int FARE_DISTANCE_LIMIT_FIRST = 10;
    private static final int FARE_DISTANCE_LIMIT_SECOND = 50;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare from(int distance) {
        return new Fare(calculateFare(distance));
    }

    public static Fare of(int distance, int extraFare, int age) {
        final int fare = calculateFare(distance) + extraFare;
        return new Fare(fare - discountForYouth(fare, age));
    }

    private static int calculateFare(int distance) {
        if (distance <= FARE_DISTANCE_LIMIT_FIRST) {
            return DEFAULT_FARE;
        }
        if (distance <= FARE_DISTANCE_LIMIT_SECOND) {
            return DEFAULT_FARE + (int) ((Math.ceil((distance - 11) / 5) + 1) * 100);
        }
        return DEFAULT_FARE_SECOND + (int) ((Math.ceil((distance - 51) / 8) + 1) * 100);
    }

    private static int discountForYouth(int fare, int age) {
        return DiscountTypeByAge.from(age).discountFare(fare);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
