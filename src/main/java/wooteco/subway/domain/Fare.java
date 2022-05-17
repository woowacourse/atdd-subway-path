package wooteco.subway.domain;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int ADDITIONAL_DISTANCE_PER_5KM = 10;
    private static final int ADDITIONAL_DISTANCE_PER_8KM = 51;
    private static final int DISTANCE_UNIT_UNDER_50 = 5;
    private static final int DISTANCE_UNIT_OVER_50 = 8;
    private static final int ADDITIONAL_AMOUNT = 100;

    public int calculate(final int distance) {
        int fare = DEFAULT_FARE;
        if (distance >= ADDITIONAL_DISTANCE_PER_5KM && distance < ADDITIONAL_DISTANCE_PER_8KM) {
            return fare + addExtraFare(distance, DISTANCE_UNIT_UNDER_50, ADDITIONAL_DISTANCE_PER_5KM);
        }
        if (distance >= ADDITIONAL_DISTANCE_PER_8KM) {
            return fare
                    + addExtraFare(ADDITIONAL_DISTANCE_PER_8KM - 1, DISTANCE_UNIT_UNDER_50, ADDITIONAL_DISTANCE_PER_5KM)
                    + addExtraFare(distance, DISTANCE_UNIT_OVER_50, ADDITIONAL_DISTANCE_PER_8KM - 1);
        }
        return fare;
    }

    private int addExtraFare(final int distance, final int distanceUnit, final int limit) {
        return (int) Math.ceil((double) (distance - limit) / distanceUnit) * ADDITIONAL_AMOUNT;
    }
}
