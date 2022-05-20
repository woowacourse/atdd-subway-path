package wooteco.subway.domain.fare;

public class MaxFareStrategy extends FareStrategy {

    @Override
    public int calculateFare(final FareCondition fareCondition) {
        return 0;
    }
}
