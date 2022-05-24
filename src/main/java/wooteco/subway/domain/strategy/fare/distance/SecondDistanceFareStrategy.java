package wooteco.subway.domain.strategy.fare.distance;

public class SecondDistanceFareStrategy implements DistanceFareStrategy {

    private static final int FIRST_INTERVAL_MAX_DISTANCE = 10;
    private static final int SECOND_INTERVAL_MAX_DISTANCE = 50;
    private static final double SECOND_INTERVAL_SURCHARGE_UNIT = 5.0;
    private static final int SECOND_INTERVAL_SURCHARGE = 100;

    @Override
    public int calculateFare(int distance) {
        int targetDistance = Math.min(distance - FIRST_INTERVAL_MAX_DISTANCE,
                SECOND_INTERVAL_MAX_DISTANCE - FIRST_INTERVAL_MAX_DISTANCE);
        return (int) Math.ceil(targetDistance / SECOND_INTERVAL_SURCHARGE_UNIT) * SECOND_INTERVAL_SURCHARGE;
    }

    @Override
    public boolean isUsable(int distance) {
        return FIRST_INTERVAL_MAX_DISTANCE < distance && distance <= SECOND_INTERVAL_MAX_DISTANCE;
    }
}
