package wooteco.subway.domain;

import java.util.Objects;
import wooteco.subway.exception.IllegalInputException;

public class Distance {

    private static final int MIN_DISTANCE = 1;
    private static final int DISTANCE_OF_BASIC_FARE = 10;
    private static final int BASIC_FARE = 1250;
    private static final int DISTANCE_OF_OVER_FARE = 50;
    private static final int STANDARD_DISTANCE_OF_OVER_FARE = 5;
    private static final int MAX_STANDARD_DISTANCE_OF_OVER_FARE = 8;
    private static final int STANDARD_OF_OVER_FARE = 100;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final int CHILD_AGE_LOWER_BOUND = 6;
    private static final int CHILD_AGE_UPPER_BOUND = 13;
    private static final int TEENAGER_AGE_LOWE_BOUND = 13;
    private static final int TEENAGER_AGE_UPPER_BOUND = 19;
    private static final int DEDUCTIBLE_AMOUNT = 350;

    private final int value;

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < MIN_DISTANCE) {
            throw new IllegalInputException("두 종점간의 거리가 유효하지 않습니다.");
        }
    }

    public void checkCanAssign(final Distance distance) {
        if (value <= distance.value) {
            throw new IllegalInputException("기존 구간의 길이 보다 작지 않습니다.");
        }
    }

    public Distance minus(final Distance distance) {
        return new Distance(value - distance.value);
    }

    public Distance plus(final Distance distance) {
        return new Distance(value + distance.value);
    }

    public Fare calculateFare(final int extraFare, final int age) {
        if (isLessThanOrEqual(DISTANCE_OF_BASIC_FARE)) {
            return new Fare(discountFare(BASIC_FARE + extraFare, age));
        }
        if (isLessThanOrEqual(DISTANCE_OF_OVER_FARE)) {
            return new Fare(
                    discountFare(BASIC_FARE +
                            extraFare +
                            calculateOverFare(value - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE), age)
            );
        }
        return new Fare(
                discountFare(BASIC_FARE +
                        extraFare +
                        calculateOverFare(DISTANCE_OF_OVER_FARE - DISTANCE_OF_BASIC_FARE,
                                STANDARD_DISTANCE_OF_OVER_FARE) +
                        calculateOverFare(value - DISTANCE_OF_OVER_FARE, MAX_STANDARD_DISTANCE_OF_OVER_FARE), age)
        );
    }

    private boolean isLessThanOrEqual(final int standardValue) {
        return value <= standardValue;
    }

    private int calculateOverFare(final int value, final int standardValue) {
        return (int) ((Math.ceil((value - 1) / standardValue) + 1) * STANDARD_OF_OVER_FARE);
    }

    private int discountFare(final int fare, final int age) {
        if (isChild(age)) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * CHILD_DISCOUNT_RATE);
        }
        if (isTeenager(age)) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * TEENAGER_DISCOUNT_RATE);
        }
        return fare;
    }

    private boolean isChild(final int age) {
        return CHILD_AGE_LOWER_BOUND <= age && age < CHILD_AGE_UPPER_BOUND;
    }

    private boolean isTeenager(final int age) {
        return TEENAGER_AGE_LOWE_BOUND <= age && age < TEENAGER_AGE_UPPER_BOUND;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Distance{" +
                "value=" + value +
                '}';
    }
}
