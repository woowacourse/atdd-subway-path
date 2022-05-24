package wooteco.subway.domain;

public class Fare {
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

    public int getValue() {
        return value;
    }
}
