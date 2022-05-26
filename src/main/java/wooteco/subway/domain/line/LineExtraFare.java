package wooteco.subway.domain.line;

import java.util.Objects;

public class LineExtraFare {

    private static final int MIN_VALUE = 0;
    private static final String INVALID_EXTRA_FARE_VALUE_EXCEPTION = "노선 추가비용은 0원 이상이어야 합니다.";

    private final int value;

    public LineExtraFare(int value) {
        validateExtraFare(value);
        this.value = value;
    }

    private void validateExtraFare(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException(INVALID_EXTRA_FARE_VALUE_EXCEPTION);
        }
    }

    public int getValue() {
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
        LineExtraFare that = (LineExtraFare) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "LineExtraFare{" + "value=" + value + '}';
    }
}
