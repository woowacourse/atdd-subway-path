package wooteco.subway.domain;

public class FareCalculator {
    private static final int MINIMUM_FARE = 1250;

    public int excute(int distanceFrom) {
        return MINIMUM_FARE + calculateOverFare(distanceFrom);
    }

    private int calculateOverFare(int distance) {
        return (int) (((distance - 10) / 5) + 1) * 100;
    }

}
