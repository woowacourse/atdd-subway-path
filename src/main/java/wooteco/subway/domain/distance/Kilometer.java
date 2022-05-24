package wooteco.subway.domain.distance;

import java.util.Objects;

public class Kilometer {

    private final int value;

    private Kilometer(int value) {
        this.value = value;
    }

    public static Kilometer from(int value) {
        return new Kilometer(value);
    }

    public boolean lessThanKm(int otherValue) {
        return value <= otherValue;
    }

    public boolean moreThanKm(int otherValue) {
        return value >= otherValue;
    }

    public boolean exceedKm(int otherValue) {
        return value > otherValue;
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
        Kilometer kilometer = (Kilometer) o;
        return value == kilometer.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
