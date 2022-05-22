package wooteco.subway.domain.Fare;

import java.util.Objects;

public class Fare {

    private final int value;

    public Fare(int fare) {
        this.value = fare;
    }

    public Fare add(int fareValue) {
        return new Fare(value + fareValue);
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
}
