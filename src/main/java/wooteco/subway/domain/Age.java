package wooteco.subway.domain;

public class Age {

    private static final int AGE_MIN_VALUE = 1;

    private final int value;

    public Age(int value) {
        validateMinValue(value);
        this.value = value;
    }

    private void validateMinValue(int value) {
        if (value < AGE_MIN_VALUE) {
            throw new IllegalArgumentException("나이는 1살 보다 어릴 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
