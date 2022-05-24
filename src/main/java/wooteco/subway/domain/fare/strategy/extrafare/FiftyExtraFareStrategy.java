package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public final class FiftyExtraFareStrategy extends ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new FiftyExtraFareStrategy();
    private static final int MIN_DISTANCE = 51;
    private static final int DISTANCE_OF_EXTRA_FARE = 50;
    private static final int STANDARD_DISTANCE_FOR_EXTRA_FARE = 8;
    private static final int BASIC_EXTRA_FARE = 800;

    private FiftyExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    boolean isMatch(final int distance) {
        return MIN_DISTANCE <= distance;
    }

    @Override
    public int apply(final Distance distance) {
        final int extraFare = calculateExtraFare(distance, DISTANCE_OF_EXTRA_FARE, STANDARD_DISTANCE_FOR_EXTRA_FARE);
        return BASIC_EXTRA_FARE + extraFare;
    }
}
