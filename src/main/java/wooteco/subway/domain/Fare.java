package wooteco.subway.domain;

import java.util.Objects;
import wooteco.subway.domain.age.AgeType;
import wooteco.subway.domain.age.FareByAgePolicy;
import wooteco.subway.domain.distance.Distance;
import wooteco.subway.domain.distance.FareByDistancePolicy;
import wooteco.subway.exception.IllegalInputException;

public class Fare {

    private final int value;

    public Fare(final int value) {
        validateFareValue(value);
        this.value = value;
    }

    public static Fare from(final Distance distance, final int extraFare, final int age) {
        return FareByDistancePolicy.apply(distance)
                .add(extraFare)
                .applyAgePolicy(age);
    }

    private Fare applyAgePolicy(final int age) {
        return FareByAgePolicy.from(AgeType.from(age))
                .applyDiscount(this);
    }

    private void validateFareValue(final int value) {
        if (value < 0) {
            throw new IllegalInputException("요금은 0보다 작을 수 없습니다.");
        }
    }

    public Fare add(final int value) {
        return new Fare(this.value + value);
    }

    public Fare minus(final int value) {
        return new Fare(this.value - value);
    }

    public Fare discount(final double percent) {
        return new Fare((int) Math.ceil((value) * (100 - percent) / 100));
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
