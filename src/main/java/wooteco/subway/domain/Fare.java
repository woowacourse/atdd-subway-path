package wooteco.subway.domain;

import java.util.Objects;
import wooteco.subway.exception.IllegalInputException;

public class Fare {

    private static final int DISTANCE_OF_BASIC_FARE = 10;
    private static final int BASIC_FARE = 1250;
    private static final int DISTANCE_OF_OVER_FARE = 50;
    private static final int STANDARD_DISTANCE_OF_OVER_FARE = 5;
    private static final int MAX_STANDARD_DISTANCE_OF_OVER_FARE = 8;
    private static final int STANDARD_OF_OVER_FARE = 100;

    private final int value;

    public Fare(final int value) {
        validateFareValue(value);
        this.value = value;
    }

    private void validateFareValue(final int value) {
        if (value < 0) {
            throw new IllegalInputException("요금은 0보다 작을 수 없습니다.");
        }
    }

    public static Fare from(final Distance distance) {
        return new Fare(calculateFare(distance));
    }

    public static int calculateFare(final Distance distance) {
        if (distance.isLessThanOrEqualByValue(DISTANCE_OF_BASIC_FARE)) {
            return BASIC_FARE;
        }

        if (distance.isLessThanOrEqualByValue(DISTANCE_OF_OVER_FARE)) {
            return BASIC_FARE +
                    calculateOverFare(distance.getValue() - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE);
        }

        return BASIC_FARE +
                calculateOverFare(DISTANCE_OF_OVER_FARE - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE) +
                calculateOverFare(distance.getValue() - DISTANCE_OF_OVER_FARE, MAX_STANDARD_DISTANCE_OF_OVER_FARE);
    }

    private static int calculateOverFare(final int value, final int standardValue) {
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
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
