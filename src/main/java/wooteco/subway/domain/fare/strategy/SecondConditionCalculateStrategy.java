package wooteco.subway.domain.fare.strategy;

public class SecondConditionCalculateStrategy implements FareCalculateStrategy {

    public static final int BASE_FARE = 2050;
    public static final double SECOND_EXTRA_FARE_STANDARD = 8;
    public static final double EXTRA_FARE = 100;

    @Override
    public int calculateFare(double distance) {
        double distanceForCharge = distance - 50;
        return BASE_FARE + (int) ((Math.ceil((distanceForCharge) / SECOND_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }
}
