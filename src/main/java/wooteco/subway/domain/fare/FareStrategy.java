package wooteco.subway.domain.fare;

public abstract class FareStrategy {

    protected static final int DEFAULT_FARE = 1250;
    protected static final int DEFAULT_DISTANCE = 10;
    protected static final int OVER_FARE_DISTANCE = 50;

    protected abstract int calculateFare(FareCondition fareCondition);
}
