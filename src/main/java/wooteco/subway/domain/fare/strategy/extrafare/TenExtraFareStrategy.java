package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public final class TenExtraFareStrategy extends ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new TenExtraFareStrategy();
    private static final int DISTANCE_OF_EXTRA_FARE = 10;
    private static final int STANDARD_DISTANCE_FOR_EXTRA_FARE = 5;

    private TenExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final Distance distance) {
        return calculateExtraFare(distance, DISTANCE_OF_EXTRA_FARE, STANDARD_DISTANCE_FOR_EXTRA_FARE);
    }
}
