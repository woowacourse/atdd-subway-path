package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public final class TenExtraFareStrategy extends ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new TenExtraFareStrategy();
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int DISTANCE_OF_EXTRA_FARE = 10;
    private static final int STANDARD_DISTANCE_FOR_EXTRA_FARE = 5;

    private TenExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    boolean isMatch(final int distance) {
        return MIN_DISTANCE < distance && distance <= MAX_DISTANCE;
    }

    @Override
    public int apply(final Distance distance) {
        return calculateExtraFare(distance, DISTANCE_OF_EXTRA_FARE, STANDARD_DISTANCE_FOR_EXTRA_FARE);
    }
}
