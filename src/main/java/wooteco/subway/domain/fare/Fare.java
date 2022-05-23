package wooteco.subway.domain.fare;

import java.util.Objects;
import wooteco.subway.exception.NotNegativeFareException;

public final class Fare {

    private final int value;

    public Fare(int value) {
        validateNotNegative(value);
        this.value = value;
    }

    private void validateNotNegative(int value) {
        if (value < 0) {
            throw new NotNegativeFareException();
        }
    }

    public Fare discount(int deduct, double discountRate) {
        int value = (int) Math.ceil((this.value - deduct) * (1 - discountRate)) + deduct;
        return new Fare(value);
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
        Fare fare = (Fare) o;
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
