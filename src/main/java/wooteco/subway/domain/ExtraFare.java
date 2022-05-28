package wooteco.subway.domain;

import wooteco.subway.exception.IllegalInputException;

public class ExtraFare {

    private final int value;

    public ExtraFare(final int value) {
        validateFareValue(value);
        this.value = value;
    }

    private void validateFareValue(final int value) {
        if (value < 0) {
            throw new IllegalInputException("추가 요금은 0보다 작을 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
