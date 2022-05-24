package wooteco.subway.domain.policy;

public class DistanceExtraFarePolicy implements FarePolicy {

    private static final int FIRST_DEFAULT_FARE = 800;
    private static final int FIRST_SURCHARGE_DISTANCE = 10;
    private static final int SECOND_SURCHARGE_DISTANCE = 50;
    private static final int FIRST_SURCHARGE_DIVIDE_DISTANCE = 5;
    private static final int SECOND_SURCHARGE_DIVIDE_DISTANCE = 8;

    private final int distance;

    public DistanceExtraFarePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int apply(int fare) {
        if (distance <= FIRST_SURCHARGE_DISTANCE) {
            return fare;
        }
        if (distance <= SECOND_SURCHARGE_DISTANCE) {
            return fare + calculateOverFare(distance - FIRST_SURCHARGE_DISTANCE,
                FIRST_SURCHARGE_DIVIDE_DISTANCE);
        }
        return fare + FIRST_DEFAULT_FARE + calculateOverFare(
            distance - SECOND_SURCHARGE_DISTANCE,
            SECOND_SURCHARGE_DIVIDE_DISTANCE);
    }

    private int calculateOverFare(int distance, int divideDistance) {
        return (int) ((Math.ceil((distance - 1) / divideDistance) + 1) * 100);
    }
}
