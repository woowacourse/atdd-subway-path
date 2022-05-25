package wooteco.subway.domain.fare.farepolicy;

public class FirstConditionCalculateStrategy implements FareCalculateStrategy {

    public static final int BASE_FARE = 1250;
    public static final double FIRST_EXTRA_FARE_STANDARD = 5;
    public static final double EXTRA_FARE = 100;

    @Override
    public int calculateFare(double distance) {
        double distanceForCharge = distance - 10;
        return BASE_FARE + (int) ((Math.ceil((distanceForCharge) / FIRST_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }
}
