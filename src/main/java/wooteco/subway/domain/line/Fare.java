package wooteco.subway.domain.line;

public class Fare {

    private final long fare;

    public Fare(long fare) {
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
