package wooteco.subway.domain.strategy.fare.distance;

public class FirstDistanceFareStrategy implements DistanceFareStrategy {

    private static final int BASIC_FARE = 1250;

    @Override
    public int calculateFare(int distance) {
        return BASIC_FARE;
    }
}
