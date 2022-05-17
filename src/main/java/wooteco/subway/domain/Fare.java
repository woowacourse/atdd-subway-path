package wooteco.subway.domain;

public class Fare {

    private static final int FIRST_DEFAULT_FARE = 1250;
    private static final int SECOND_DEFAULT_FARE = 2050;
    private static final int FIRST_STANDARD_FARE_DISTANCE = 10;
    private static final int SECOND_STANDARD_FARE_DISTANCE = 50;

    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance > FIRST_STANDARD_FARE_DISTANCE && distance <= SECOND_STANDARD_FARE_DISTANCE) {
            return FIRST_DEFAULT_FARE + calculateFirstOverFare(distance - FIRST_STANDARD_FARE_DISTANCE);
        }
        if (distance > SECOND_STANDARD_FARE_DISTANCE) {
            return SECOND_DEFAULT_FARE + calculateSecondOverFare(distance - SECOND_STANDARD_FARE_DISTANCE);
        }
        return FIRST_DEFAULT_FARE;
    }

    private int calculateFirstOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateSecondOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
