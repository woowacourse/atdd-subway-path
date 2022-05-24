package wooteco.subway.domain.strategy.fare.distance;

public class FirstDistanceFareStrategy implements DistanceFareStrategy {

    private static final int FIRST_INTERVAL_DISTANCE_MINIMUM = 0;
    private static final int FIRST_INTERVAL_DISTANCE_MAXIMUM = 10;
    private static final int BASIC_FARE = 1250;

    @Override
    public int calculateFare(int distance) {
        return BASIC_FARE;
    }

    public boolean isUsable(int distance) {
        return FIRST_INTERVAL_DISTANCE_MINIMUM <= distance && distance <= FIRST_INTERVAL_DISTANCE_MAXIMUM;
    }
}
