package wooteco.subway.domain;

import java.util.Objects;

public class ExtraFare {

    private int extraFare;

    public ExtraFare(int extraFare) {
        validateNotUnderZero(extraFare);
        this.extraFare = extraFare;
    }

    private void validateNotUnderZero(int extraFare) {
        if (extraFare < 0 ) {
            throw new IllegalArgumentException("추가 요금은 음수가 될 수 없습니다.");
        }
    }

    public int getExtraFare() {
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
        ExtraFare extraFare1 = (ExtraFare) o;
        return extraFare == extraFare1.extraFare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(extraFare);
    }
}
