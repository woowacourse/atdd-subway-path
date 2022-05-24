package wooteco.subway.domain.fare;

import java.util.Objects;

public class Fare {

    private final int value;

    public Fare(int fare) {
        this.value = fare;
    }

    public Fare add(int fareValue) {
        return new Fare(value + fareValue);
    }

    public Fare add(Fare extraFare) {
        return new Fare(value + extraFare.value);
    }

    public Fare minus(int value) {
        return new Fare(this.value - value);
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

    public int value() {
        return value;
    }

    public Fare discountPercent(int percent) {
        return new Fare((int) (value / 100.0) * (100 - percent));
    }
}
