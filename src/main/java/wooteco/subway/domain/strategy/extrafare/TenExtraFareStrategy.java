package wooteco.subway.domain.strategy.extrafare;

import wooteco.subway.domain.Distance;

public class TenExtraFareStrategy implements ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new TenExtraFareStrategy();

    private static final int DISTANCE_OF_BASIC_FARE = 10;
    private static final int STANDARD_OF_OVER_FARE = 100;
    private static final int STANDARD_DISTANCE_OF_OVER_FARE = 5;

    private TenExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final Distance distance) {
        return calculateOverFare(distance.getValue() - DISTANCE_OF_BASIC_FARE,
                STANDARD_DISTANCE_OF_OVER_FARE);
    }

    private int calculateOverFare(final int value, final int standardValue) {
        return (int) ((Math.ceil((value - 1) / standardValue) + 1) * STANDARD_OF_OVER_FARE);
    }
}
