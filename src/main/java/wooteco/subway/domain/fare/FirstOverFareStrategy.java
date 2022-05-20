package wooteco.subway.domain.fare;

public class FirstOverFareStrategy extends OverFareStrategy {

    private static final int STANDARD_UNIT = 5;

    @Override
    public int calculateFare(final FareCondition fareCondition) {
        return DEFAULT_FARE
                + calculateOverFare(fareCondition.getDistance() - DEFAULT_DISTANCE, STANDARD_UNIT)
                + fareCondition.getExtraFare();
    }
}
