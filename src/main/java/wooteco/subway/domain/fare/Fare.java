package wooteco.subway.domain.fare;

import java.util.Objects;
import wooteco.subway.domain.distance.Kilometer;

public class Fare {

    private static final int MINIMUM_FARE = 0;
    private static final double PERCENT_MAX_VALUE = 100.0;
    private static final int BASIC = 1250;
    private final int value;

    public Fare(int fare) {
        this.value = fare;
        validateValue();
    }

    public static Fare basic() {
        return new Fare(BASIC);
    }

    private void validateValue() {
        if (value < MINIMUM_FARE) {
            throw new IllegalArgumentException("0 미만의 값으로 요금이 될 수 없습니다.");
        }
    }

    public Fare add(int fareValue) {
        return new Fare(value + fareValue);
    }

    public Fare add(Fare extraFare) {
        return new Fare(value + extraFare.value);
    }

    public Fare discount(int value) {
        if (this.value - value < MINIMUM_FARE) {
            throw new IllegalArgumentException("현재 요금보다 비싼 가격을 할인할 수 없습니다.");
        }
        return new Fare(this.value - value);
    }

    public Fare discountPercent(int percent) {
        if (percent > PERCENT_MAX_VALUE) {
            return new Fare(0);
        }
        return new Fare((int) (value - value * (percent / PERCENT_MAX_VALUE)));
    }

    public Fare apply(Kilometer distance) {
        return DistanceFarePolicy.apply(this, distance);
    }

    public Fare apply(Age age) {
        return AgeFarePolicy.apply(this, age);
    }

    public int value() {
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
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "원";
    }
}
