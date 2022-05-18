package wooteco.subway.domain.strategy;

public class BasicFareStrategy implements FareStrategy {

    private static final int BASE_DISTANCE = 10;
    private static final int MIN_EXTRA_FARE = 0;
    private static final int FIRST_ROLE_STANDARD_DISTANCE = 40;
    private static final int FIRST_ROLE_EXTRA_FARE_STANDARD = 5;
    private static final int SECOND_ROLE_EXTRA_FARE_STANDARD = 8;

    @Override
    public int calculate(int distance) {
        int overDistance = distance - BASE_DISTANCE;
        return Math.max(calculateOverFare(overDistance), MIN_EXTRA_FARE);
    }

    private int calculateOverFare(int distance) {
        if (distance > FIRST_ROLE_STANDARD_DISTANCE) {
            return (int) ((Math.ceil((FIRST_ROLE_STANDARD_DISTANCE - 1) / FIRST_ROLE_EXTRA_FARE_STANDARD) + 1) * 100)
                    + (int) ((Math.ceil((distance - FIRST_ROLE_STANDARD_DISTANCE - 1) / SECOND_ROLE_EXTRA_FARE_STANDARD) + 1) * 100);
        }
        return (int) ((Math.ceil((distance - 1) / FIRST_ROLE_EXTRA_FARE_STANDARD) + 1) * 100);
    }
}
