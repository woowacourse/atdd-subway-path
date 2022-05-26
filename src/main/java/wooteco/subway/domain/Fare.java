package wooteco.subway.domain;

import java.util.Objects;

public class Fare {

    private static final int OVER_FARE_PRICE = 100;
    private static final int MINIMUM_FARE_PRICE = 1250;
    private static final int MINIMUM_DISTANCE_BOUNDARY = 10;
    private static final int MAXIMUM_DISTANCE_BOUNDARY = 50;
    private static final int BELOW_FIFTY_KM_POLICY = 5;
    private static final int ABOVE_FIFTY_KM_POLICY = 8;
    private static final int MAXIMUM_ADDITIONAL_FAIR_PRICE_BELOW_FIFTY_KM_POLICY = 800;

    private final int amount;

    public Fare(final int amount) {
        this.amount = amount;
    }

    public static Fare of(final int distance,
                          final int extraFareByLine,
                          final AgeDiscountPolicy ageDiscountPolicy) {
        final int fare = calculateFareByDistance(distance) + extraFareByLine;
        final int discountedFare = ageDiscountPolicy.getDiscountedFare(fare);

        return new Fare(discountedFare);
    }

    private static int calculateFareByDistance(final double distance) {
        if (distance <= MINIMUM_DISTANCE_BOUNDARY) {
            return MINIMUM_FARE_PRICE;
        }

        return MINIMUM_FARE_PRICE + calculateOverFare(distance);
    }

    private static int calculateOverFare(final double distance) {
        if (distance <= MAXIMUM_DISTANCE_BOUNDARY) {
            return calculateByPolicy(distance - MINIMUM_DISTANCE_BOUNDARY, BELOW_FIFTY_KM_POLICY);
        }

        return MAXIMUM_ADDITIONAL_FAIR_PRICE_BELOW_FIFTY_KM_POLICY +
                calculateByPolicy(distance - MAXIMUM_DISTANCE_BOUNDARY, ABOVE_FIFTY_KM_POLICY);
    }

    private static int calculateByPolicy(final double overFaredDistance, final int policy) {
        return (int) ((Math.ceil((overFaredDistance) / policy)) * OVER_FARE_PRICE);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Fare)) return false;
        final Fare fare = (Fare) o;
        return amount == fare.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
