package wooteco.subway.domain;

import java.util.Objects;

public class Fare {
    private static final int BASE_FARE = 1250;
    private static final int MINIMUM_BOUNDARY = 10;
    private static final int MAXIMUM_BOUNDARY = 50;
    private static final int OVER_TEN_POLICY = 5;
    private static final int OVER_FIFTY_POLICY = 8;
    private static final int SURCHARGE = 100;

    private final int amount;

    public Fare(int distance) {
        this.amount = calculateFare(distance);
    }

    public int calculateFare(int distance) {
        int fare = BASE_FARE;

        fare = belowMaximumBoundary(fare, distance);
        fare = overMaximumBoundary(fare, distance);

        return fare;
    }

    private int belowMaximumBoundary(int fare, int distance) {
        if (MINIMUM_BOUNDARY < distance && distance <= MAXIMUM_BOUNDARY) {
            fare += calculateOverFare(distance - MINIMUM_BOUNDARY, OVER_TEN_POLICY);
        }
        return fare;
    }

    private int overMaximumBoundary(int fare, int distance) {
        if (MAXIMUM_BOUNDARY < distance) {
            fare += calculateOverFare(MAXIMUM_BOUNDARY - MINIMUM_BOUNDARY, OVER_TEN_POLICY);
            fare += calculateOverFare(distance - MAXIMUM_BOUNDARY, OVER_FIFTY_POLICY);
        }
        return fare;
    }

    private int calculateOverFare(int overDistance, int policy) {
        return (int) ((Math.ceil((overDistance - 1) / policy) + 1) * SURCHARGE);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return amount == fare.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
