package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public abstract class ExtraFareStrategy {

    private static final int EXTRA_FARE_PER_DISTANCE = 100;

    public abstract int calculate(final Distance distance);

    protected int calculateExtraFare(final Distance distance, final int distanceOfExtraFare,
                                     final int standardDistanceForExtraFare) {
        final int distanceToChargeExtraFare = distance.getValue() - distanceOfExtraFare;
        final double distanceDividedByStandard =
                Math.ceil((distanceToChargeExtraFare - 1) / standardDistanceForExtraFare) + 1;
        final double extraFare = distanceDividedByStandard * EXTRA_FARE_PER_DISTANCE;
        return (int) extraFare;
    }
}
