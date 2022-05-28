package wooteco.subway.domain;

import java.util.Objects;

public class Fare {

    private static final String INVALIE_FARE_EXCEPTION = "요금은 0 이상이여야 합니다.";
    private static final int MINIMUM_FARE = 0;

    private final int value;

    public Fare(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_FARE) {
            throw new IllegalArgumentException(INVALIE_FARE_EXCEPTION);
        }
    }

    public int getValue() {
        return value;
    }

    public Fare mul(double times) {
        return new Fare((int) (value * times));
    }

    public Fare sub(int subValue) {
        return new Fare(value - subValue);
    }

    public static Fare sum(Fare fare1, Fare fare2) {
        return new Fare(fare1.value + fare2.value);
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
}
