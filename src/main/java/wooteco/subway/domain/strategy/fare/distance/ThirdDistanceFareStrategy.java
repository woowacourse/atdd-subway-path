package wooteco.subway.domain.strategy.fare.distance;

public class ThirdDistanceFareStrategy implements DistanceFareStrategy {

    private static final int THIRD_SURCHARGE = 100;
    private static final double THIRD_INTERVAL_SURCHARGE_UNIT = 8.0;
    private static final int THIRD_INTERVAL_MAX_DISTANCE = 50;

    @Override
    public int calculateFare(int distance) {
        return calculateIntervalTwo(distance);
    }

    @Override
    public boolean isUsable(int distance) {
        return distance > THIRD_INTERVAL_MAX_DISTANCE;
    }

    private int calculateIntervalTwo(int distance) {
        int targetDistance = distance - THIRD_INTERVAL_MAX_DISTANCE;
        return (int) Math.ceil(targetDistance / THIRD_INTERVAL_SURCHARGE_UNIT) * THIRD_SURCHARGE;
    }
}
