package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public class FiftyExtraFareStrategy implements ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new FiftyExtraFareStrategy();

    private static final int DISTANCE_OF_BASIC_FARE = 10;
    private static final int DISTANCE_OF_OVER_FARE = 50;
    private static final int STANDARD_OF_OVER_FARE = 100;
    private static final int STANDARD_DISTANCE_OF_OVER_FARE = 5;
    private static final int MAX_STANDARD_DISTANCE_OF_OVER_FARE = 8;

    private FiftyExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final Distance distance) {
        return calculateOverFare(DISTANCE_OF_OVER_FARE - DISTANCE_OF_BASIC_FARE,
                STANDARD_DISTANCE_OF_OVER_FARE) +
                calculateOverFare(distance.getValue() - DISTANCE_OF_OVER_FARE, MAX_STANDARD_DISTANCE_OF_OVER_FARE);
    }

    private int calculateOverFare(final int value, final int standardValue) {
        return (int) ((Math.ceil((value - 1) / standardValue) + 1) * STANDARD_OF_OVER_FARE);
    }
}
