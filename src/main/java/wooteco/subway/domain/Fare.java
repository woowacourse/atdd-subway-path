package wooteco.subway.domain;

public class Fare {

    private static final double DEFAULT_FARE = 1250;
    private static final double MAXIMUM_DISTANCE_OF_DEFAULT_FARE = 10;
    private static final double MINIMUM_DISTANCE_OF_MAXIMUM_FARE = 50;
    private static final double DISTANCE_UNIT_UNDER_50 = 5;
    private static final double DISTANCE_UNIT_OVER_50 = 8;
    private static final double ADDITIONAL_AMOUNT = 100;

    public double calculate(final double distance) {
        double fare = DEFAULT_FARE;
        if (distance <= MAXIMUM_DISTANCE_OF_DEFAULT_FARE) {
            return fare;
        }
        if (distance <= MINIMUM_DISTANCE_OF_MAXIMUM_FARE) {
            return fare + addExtraFare(distance, DISTANCE_UNIT_UNDER_50, MAXIMUM_DISTANCE_OF_DEFAULT_FARE);
        }
        return fare
                + addExtraFare(MINIMUM_DISTANCE_OF_MAXIMUM_FARE, DISTANCE_UNIT_UNDER_50, MAXIMUM_DISTANCE_OF_DEFAULT_FARE)
                + addExtraFare(distance, DISTANCE_UNIT_OVER_50, MINIMUM_DISTANCE_OF_MAXIMUM_FARE);
    }

    private double addExtraFare(final double distance, final double distanceUnit, final double limit) {
        return Math.ceil((distance - limit) / distanceUnit) * ADDITIONAL_AMOUNT;
    }
}
