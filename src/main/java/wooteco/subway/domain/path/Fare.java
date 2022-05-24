package wooteco.subway.domain.path;

import java.util.Arrays;

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

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Fare otherFare) {
        return Integer.compare(this.value, otherFare.value);
    }
}
