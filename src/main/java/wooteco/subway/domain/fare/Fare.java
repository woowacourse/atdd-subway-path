package wooteco.subway.domain.fare;

import java.util.Objects;
import wooteco.subway.domain.fare.policy.DiscountPolicy;
import wooteco.subway.domain.fare.policy.FareByDistancePolicy;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.exception.NegativeFareException;

public class Fare {

    private final int value;

    public Fare(int value) {
        validatePositiveFare(value);
        this.value = value;
    }

    public static Fare of(Distance distance, Age age, Fare extraFare) {
        FareByDistancePolicy fareByDistancePolicy = FareByDistancePolicy.from(distance);
        DiscountPolicy discountPolicy = DiscountPolicy.from(age);

        Fare fareByDistance = fareByDistancePolicy.calculate(distance);
        Fare addedExtraFare = fareByDistance.add(extraFare);
        return discountPolicy.discount(addedExtraFare);
    }

    private void validatePositiveFare(int fare) {
        if (fare < 0) {
            throw new NegativeFareException();
        }
    }

    public Fare add(Fare other) {
        return new Fare(value + other.value);
    }

    public Fare subtract(int operand) {
        return new Fare(value - operand);
    }

    public Fare multiply(double rate) {
        int multiplied = (int) (value * rate);
        return new Fare(multiplied);
    }

    public Fare discountWithAge(Age age) {
        return DiscountPolicy.from(age).discount(this);
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
