package wooteco.subway.domain.fare;

public class MaxFareStrategy extends OverFareStrategy {

    private static final int MAX_UNIT = 8;

    @Override
    public int calculateFare(final FareCondition fareCondition) {
        return DEFAULT_FARE
                + calculateOverFare(OVER_FARE_DISTANCE - DEFAULT_DISTANCE, STANDARD_UNIT)
                + calculateOverFare(fareCondition.getDistance() - OVER_FARE_DISTANCE, MAX_UNIT)
                + fareCondition.getExtraFare();
    }
}
