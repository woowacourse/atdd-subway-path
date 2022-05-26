package wooteco.subway.domain.fare.strategy;

public class SpecialExtraFareStrategy implements ExtraFareStrategy {

    private static double SECOND_ROLE_EXTRA_FARE_STANDARD = 8;
    private static final int START_SPECIAL_EXTRA_FARE_DISTANCE = 50;
    private static final int MAX_BASIC_EXTRA_FARE = 800;

    @Override
    public int calculate(int distance) {
        int target = distance - START_SPECIAL_EXTRA_FARE_DISTANCE;
        return ((int) (Math.ceil((target) / SECOND_ROLE_EXTRA_FARE_STANDARD))
                * FARE_UNIT) + MAX_BASIC_EXTRA_FARE;
    }

    @Override
    public boolean isMatch(int distance) {
        return START_SPECIAL_EXTRA_FARE_DISTANCE < distance;
    }
}
