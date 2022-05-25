package wooteco.subway.domain.fare.distancestrategy;

public class SecondDistanceStrategy implements DistanceStrategy {

    private static final int STANDARD_KILOMETER = 8;
    private static final int ADDED_FARE = 100;
    private static final int DISTANCE_LOWER_BOUND = 50;

    @Override
    public int getPrice(int distance) {
        return new FirstDistanceStrategy().getPrice(DISTANCE_LOWER_BOUND)
                + calculateOverFare(distance - DISTANCE_LOWER_BOUND);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / STANDARD_KILOMETER) + 1) * ADDED_FARE);
    }
}
