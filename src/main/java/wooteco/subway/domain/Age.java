package wooteco.subway.domain;

public class Age {

    private static final int MINIMUM_AGE = 0;
    private static final String INVALID_AGE_EXCEPTION = "나이는 0살 이상이여야 합니다.";

    private final int value;

    public Age(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value < MINIMUM_AGE) {
            throw new IllegalArgumentException(INVALID_AGE_EXCEPTION);
        }
    }

    public boolean isChildren() {
        return value >= 6 && value < 13;
    }

    public boolean isTeenager() {
        return value >= 13 && value < 19;
    }

    public boolean isOther() {
        return value < 6 || value >= 19;
    }
}
