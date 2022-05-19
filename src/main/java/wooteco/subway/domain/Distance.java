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

    public Fare calculateFare() {
        if (isLessThanOrEqual(DISTANCE_OF_BASIC_FARE)) {
            return new Fare(BASIC_FARE);
        }
        if (isLessThanOrEqual(DISTANCE_OF_OVER_FARE)) {
            return new Fare(
                    BASIC_FARE +
                            calculateOverFare(value - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE)
            );
        }
        return new Fare(
                BASIC_FARE +
                        calculateOverFare(DISTANCE_OF_OVER_FARE - DISTANCE_OF_BASIC_FARE,
                                STANDARD_DISTANCE_OF_OVER_FARE) +
                        calculateOverFare(value - DISTANCE_OF_OVER_FARE, MAX_STANDARD_DISTANCE_OF_OVER_FARE)
        );
    }

    private boolean isLessThanOrEqual(final int standardValue) {
        return value <= standardValue;
    }

    private int calculateOverFare(final int value, final int standardValue) {
        return (int) ((Math.ceil((value - 1) / standardValue) + 1) * STANDARD_OF_OVER_FARE);
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
