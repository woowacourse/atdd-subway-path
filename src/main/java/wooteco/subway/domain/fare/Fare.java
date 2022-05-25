package wooteco.subway.domain.fare;

public class Fare {

    private static final String DISTANCE_NEGATIVE_ERROR = "거리는 음수가 될 수 없습니다.";

    private final int distance;

    public Fare(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(DISTANCE_NEGATIVE_ERROR);
        }
    }

    public int calculate(int age, int extraFare) {
        int fare = FarePolicy.calculate(distance) + extraFare;
        return DiscountPolicy.calculate(age, fare);
    }
}
