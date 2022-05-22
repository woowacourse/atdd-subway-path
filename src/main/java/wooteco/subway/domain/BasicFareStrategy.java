package wooteco.subway.domain;

public class BasicFareStrategy implements FareStrategy {

    private static final int BASIC_FARE = 1250;
    private static final int MIN_EXTRA_OVER_FARE = 0;
    private static final int FIRST_OVER_DISTANCE = 10;
    private static final int SECOND_OVER_DISTANCE = 50;
    private static final int OVER_FARE_STANDARD = 100;
    private static final int FIRST_OVER_DISTANCE_STANDARD = 5;
    private static final int SECOND_OVER_DISTANCE_STANDARD = 8;
    private static final int MAX_FARE_FROM_TEN_TO_FIFTY = 800;

    @Override
    public int calculate(int distance) {
        if (distance > SECOND_OVER_DISTANCE) {
            return BASIC_FARE + MAX_FARE_FROM_TEN_TO_FIFTY + calculateOverFifty(distance - SECOND_OVER_DISTANCE);
        }

        int overDistance = distance - FIRST_OVER_DISTANCE;
        return BASIC_FARE + Math.max(calculateOverFromTenToFifty(overDistance), MIN_EXTRA_OVER_FARE);
    }

    private int calculateOverFromTenToFifty(int distance) {
        return (int) ((Math.ceil((distance - 1) / FIRST_OVER_DISTANCE_STANDARD) + 1) * OVER_FARE_STANDARD);
    }

    private int calculateOverFifty(int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / SECOND_OVER_DISTANCE_STANDARD) + 1) * OVER_FARE_STANDARD);
    }
}
