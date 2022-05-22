package wooteco.subway.domain.property;

import wooteco.subway.exception.InvalidRequestException;

public class Age {
    private static final int MIN_OF_TEENAGER = 13;
    private static final int MAX_OF_TEENAGER = 18;
    private static final int MIN_OF_CHILDREN = 6;
    private static final int MAX_OF_CHILDREN = 12;

    private final int value;

    public Age(Integer value) {
        validatePresent(value);
        validatePositive(value);
        this.value = value;
    }

    private void validatePresent(Integer value) {
        if (value == null) {
            throw new InvalidRequestException("나이는 필수 입력값입니다.");
        }
    }

    private void validatePositive(Integer value) {
        if (value <= 0) {
            throw new InvalidRequestException("나이는 0 이하가 될 수 없습니다.");
        }
    }

    public boolean isTeenager() {
        return value >= MIN_OF_TEENAGER && value <= MAX_OF_TEENAGER;
    }

    public boolean isChildren() {
        return value >= MIN_OF_CHILDREN && value <= MAX_OF_CHILDREN;
    }

    public int getValue() {
        return value;
    }
}
