package wooteco.subway.domain;

public class Fare {

    private static final int FIRST_DEFAULT_FARE = 1250;
    private static final int SECOND_DEFAULT_FARE = 2050;
    private static final int FIRST_STANDARD_FARE_DISTANCE = 10;
    private static final int SECOND_STANDARD_FARE_DISTANCE = 50;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_ADDITIONAL_STANDARD = 5;
    private static final int SECOND_ADDITIONAL_STANDARD = 8;

    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public int calculateFare() {
        if (distance > FIRST_STANDARD_FARE_DISTANCE && distance <= SECOND_STANDARD_FARE_DISTANCE) {
            return FIRST_DEFAULT_FARE + calculateFirstOverFare(distance - FIRST_STANDARD_FARE_DISTANCE);
        }

        if (distance > SECOND_STANDARD_FARE_DISTANCE) {
            return SECOND_DEFAULT_FARE + calculateSecondOverFare(distance - SECOND_STANDARD_FARE_DISTANCE);
        }

        return FIRST_DEFAULT_FARE;
    }

    private int calculateFirstOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / FIRST_ADDITIONAL_STANDARD) + 1) * ADDITIONAL_FARE);
    }

    private int calculateSecondOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / SECOND_ADDITIONAL_STANDARD) + 1) * ADDITIONAL_FARE);
    }
}
