package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.Objects;

public class Fare implements Comparable<Fare> {
    private final int value;

    public Fare(int value) {
        checkValue(value);
        this.value = value;
    }

    private void checkValue(int value) {
        checkNegative(value);
        checkUnit(value);
    }

    private void checkNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("요금은 음수일 수 없습니다.");
        }
    }

    private void checkUnit(int value) {
        if ((value % 10) != 0) {
            throw new IllegalArgumentException("요금은 최소 10원 단위여야 합니다.");
        }
    }

    static Fare sum(Fare... fares) {
        int totalAmount = Arrays.stream(fares)
                .mapToInt(fare -> fare.value)
                .sum();
        return new Fare(totalAmount);
    }

    Fare subtract(Fare fare) {
        return new Fare(this.value - fare.value);
    }

    Fare discount(double rate) {
        int discountAmount = (int) Math.ceil(this.value * rate);
        return new Fare(this.value - discountAmount);
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Fare otherFare) {
        return Integer.compare(this.value, otherFare.value);
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
