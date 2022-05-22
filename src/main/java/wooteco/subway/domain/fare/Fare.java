package wooteco.subway.domain.fare;

import java.util.Objects;

public class Fare {
    private static final int BASE_FARE = 1250;
    private static final int MINIMUM_BOUNDARY = 10;
    private static final int MAXIMUM_BOUNDARY = 50;
    private static final int OVER_TEN_POLICY = 5;
    private static final int OVER_FIFTY_POLICY = 8;
    private static final int SURCHARGE = 100;

    private final double amount;

    public Fare(int distance, int lineFare, int age) {
        FareCalculator fareCalculator = new FareCalculator(Age.from(age));
        this.amount = fareCalculator.calculateAgeFare(calculateDistanceFare(distance) + lineFare);
    }

    public int calculateDistanceFare(int distance) {
        if(distance <= MINIMUM_BOUNDARY){
            return BASE_FARE;
        }
        if (distance <= MAXIMUM_BOUNDARY) {
            return BASE_FARE + belowMaximumBoundary(distance);
        }
        return BASE_FARE + calculateOverFare(MAXIMUM_BOUNDARY - MINIMUM_BOUNDARY, OVER_TEN_POLICY) + overMaximumBoundary(distance);
    }

    private int belowMaximumBoundary (int distance) {
        return calculateOverFare(distance - MINIMUM_BOUNDARY, OVER_TEN_POLICY);
    }

    private int overMaximumBoundary(int distance) {
        return calculateOverFare(distance - MAXIMUM_BOUNDARY, OVER_FIFTY_POLICY);
    }

    private int calculateOverFare(int overDistance, int policy) {
        return (int) ((Math.ceil((overDistance - 1) / policy) + 1) * SURCHARGE);
    }

    public double getAmount() {
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
