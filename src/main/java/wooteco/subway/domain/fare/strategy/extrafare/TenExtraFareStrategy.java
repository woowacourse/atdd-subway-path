package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public class TenExtraFareStrategy implements ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new TenExtraFareStrategy();

    private static final int DISTANCE_OF_EXTRA_FARE = 10;
    private static final int EXTRA_FARE_PER_DISTANCE = 100;
    private static final int STANDARD_DISTANCE_FOR_EXTRA_FARE = 5;

    private TenExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final Distance distance) {
        final int distanceToChargeExtraFare = distance.getValue() - DISTANCE_OF_EXTRA_FARE;
        final double distanceDividedByStandard =
                Math.ceil((distanceToChargeExtraFare - 1) / STANDARD_DISTANCE_FOR_EXTRA_FARE) + 1;
        final double extraFare = distanceDividedByStandard * EXTRA_FARE_PER_DISTANCE;
        return (int) extraFare;
    }
}
