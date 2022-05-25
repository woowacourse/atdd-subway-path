package wooteco.subway.domain.vo;

import java.util.Objects;

public class LineExtraFare {

    private final long extraFare;

    private LineExtraFare(Long input) {
        this.extraFare = validate(input);
    }

    public static LineExtraFare from(Long input) {
        return new LineExtraFare(input);
    }

    private long validate(Long input) {
        validateNotNull(input);
        validatePositive(input);

        return input;
    }

    private void validateNotNull(Long input) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("노선 추가 요금은 null일 수 없습니다");
        }
    }

    private void validatePositive(long input) {
        if (input < 0) {
            throw new IllegalArgumentException("노선 추가 요금은 0 이상이어야 합니다 : " + input);
        }
    }

    public long getExtraFare() {
        return extraFare;
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
        return Objects.equals(extraFare, that.extraFare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extraFare);
    }
}
