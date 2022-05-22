package wooteco.subway.domain.fare;

import java.util.Objects;
import wooteco.subway.domain.fare.discountstrategy.DiscountStrategyFactory;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.exception.NegativeFareException;

public class Fare {

    public static final int DEFAULT_FARE = 1250;
    public static final int OVER_FARE_AT_50 = 800;
    public static final int OVER_FARE_AMOUNT_PER_UNIT_DISTANCE = 100;
    public static final int NO_OVER_FARE_DISTANCE = 10;
    public static final int FIRST_OVER_FARE_DISTANCE = 50;
    public static final int FIRST_OVER_FARE_UNIT_DISTANCE = 5;
    public static final int SECOND_OVER_FARE_UNIT_DISTANCE = 8;

    private final int value;

    public Fare(int value) {
        validatePositiveFare(value);
        this.value = value;
    }

    public static Fare from(Distance distance) {
        return new Fare(DEFAULT_FARE + calculateOverFare(distance.getValue()));
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

    private void validatePositiveFare(int fare) {
        if (fare < 0) {
            throw new NegativeFareException();
        }
    }

    public Fare subtract(int operand) {
        return new Fare(value - operand);
    }

    public Fare multiply(double rate) {
        int multiplied = (int) (value * rate);
        return new Fare(multiplied);
    }

    public Fare discountWithAge(Age age) {
        DiscountStrategyFactory discountStrategyFactory = new DiscountStrategyFactory();
        return discountStrategyFactory.getDiscountStrategy(age).discount(this);
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
        Fare fare1 = (Fare) o;
        return value == fare1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare=" + value +
                '}';
    }
}
