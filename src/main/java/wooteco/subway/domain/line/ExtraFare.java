package wooteco.subway.domain.line;

public class ExtraFare {

    private final long fare;

    public ExtraFare(long fare) {
        validateFareNotNegative(fare);
        this.fare = fare;
    }

    private void validateFareNotNegative(long fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 음수가 될 수 없습니다.");
        }
    }

    public long getFare() {
        return fare;
    }
}
