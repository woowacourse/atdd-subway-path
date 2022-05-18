package wooteco.subway.domain;

public class BasicFareStrategy implements FareStrategy {

    private static final int BASIC_DISTANCE = 10;

    @Override
    public int calculate(int distance) {
        int overDistance = distance - BASIC_DISTANCE;
        return Math.max(calculateOverFare(overDistance), 0);
    }

    private int calculateOverFare(int distance) {
        if (distance > 40) {
            return (int) ((Math.ceil((40 - 1) / 5) + 1) * 100)
                    + (int) ((Math.ceil((distance - 41) / 8) + 1) * 100);
        }
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
