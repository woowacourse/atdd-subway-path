package wooteco.subway.domain.property;

import wooteco.subway.exception.InvalidRequestException;

public class Age {

    private static final int CHILD_AGE_THRESHOLD = 6;
    private static final int TEENAGER_AGE_THRESHOLD = 13;
    private static final int ADULT_AGE_THRESHOLD = 19;

    private final int value;

    public Age(int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(int value) {
        if (value <= 0) {
            throw new InvalidRequestException("나이는 음수일 수 없습니다.");
        }
    }

    public boolean isTeenager() {
        return TEENAGER_AGE_THRESHOLD <= this.value && this.value < ADULT_AGE_THRESHOLD;
    }

    public boolean isChild() {
        return CHILD_AGE_THRESHOLD <= this.value && this.value < TEENAGER_AGE_THRESHOLD;
    }

    public int getValue() {
        return value;
    }
}
