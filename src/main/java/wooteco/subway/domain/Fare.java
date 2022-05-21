package wooteco.subway.domain;

public class Fare {

    private static final int STANDARD_DISTANCE = 10;
    private static final int STANDARD_FARE = 1250;
    private static final int OVER_FARE_UNIT = 100;
    private static final int FIRST_OVER_FARE_DISTANCE = 50;
    private static final int FIRST_OVER_DISTANCE_UNIT = 5;
    private static final int SECOND_OVER_DISTANCE_UNIT = 8;

    private final int value;

    private Fare(final int value) {
        this.value = value;
    }

    public static Fare from(final int distance) {
        if (distance <= STANDARD_DISTANCE) {
            return new Fare(STANDARD_FARE);
        }
        if (distance <= FIRST_OVER_FARE_DISTANCE) {
            return new Fare(STANDARD_FARE
                    + calculateOverFare(distance - STANDARD_DISTANCE, FIRST_OVER_DISTANCE_UNIT));
        }
        return new Fare(STANDARD_FARE
                + calculateOverFare(FIRST_OVER_FARE_DISTANCE - STANDARD_DISTANCE, FIRST_OVER_DISTANCE_UNIT)
                + calculateOverFare(distance - FIRST_OVER_FARE_DISTANCE, SECOND_OVER_DISTANCE_UNIT));
    }

    private static int calculateOverFare(final int distance, final int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * OVER_FARE_UNIT);
    }

    public int getValue() {
        return value;
    }
}
