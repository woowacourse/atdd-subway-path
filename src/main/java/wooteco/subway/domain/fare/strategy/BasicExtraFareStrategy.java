package wooteco.subway.domain.fare.strategy;


public class BasicExtraFareStrategy implements ExtraFareStrategy {

    private static double BASIC_EXTRA_FARE_STANDARD = 5;
    private static final int NO_EXTRA_STANDARD_DISTANCE = 10;
    private static final int BASIC_EXTRA_FARE_MAX_DISTANCE = 50;

    @Override
    public int calculate(int distance) {
        return (int) (Math.ceil((distance - NO_EXTRA_STANDARD_DISTANCE) / BASIC_EXTRA_FARE_STANDARD))
                * FARE_UNIT;
    }

    @Override
    public boolean isMatch(int distance) {
        return NO_EXTRA_STANDARD_DISTANCE < distance &&
                BASIC_EXTRA_FARE_MAX_DISTANCE >= distance;
    }
}
