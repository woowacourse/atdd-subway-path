package wooteco.subway.domain;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int MIN_DISTANCE_UNIT = 5;
    private static final int MAX_DISTANCE_UNIT = 8;
    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int MAX_DISTANCE_USE_MIN_DISTANCE_UNIT = 50;
    private static final int ADDITIONAL_FARE = 100;
    private static final int MAX_ADDITIONAL_FARE_USE_MIN_DISTANCE_UNIT = ADDITIONAL_FARE * 8;

    private final int fare;

    public Fare(double distance) {
        fare = calculateFare((int) distance);
    }

    private int calculateFare(int distance) {
        if (distance > DEFAULT_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateOverFare(distance);
        }

        return DEFAULT_FARE;
    }

    private int calculateOverFare(int distance) {
        if (distance <= MAX_DISTANCE_USE_MIN_DISTANCE_UNIT) {
            var overDistance = distance - DEFAULT_FARE_DISTANCE;
            return calculateOverFare(overDistance, MIN_DISTANCE_UNIT);
        }

        var overDistance = distance - MAX_DISTANCE_USE_MIN_DISTANCE_UNIT;
        return calculateOverFare(overDistance, MAX_DISTANCE_UNIT) + MAX_ADDITIONAL_FARE_USE_MIN_DISTANCE_UNIT;
    }

    private int calculateOverFare(int overDistance, int distanceUnit) {
        return (int) ((Math.ceil((overDistance - 1) / distanceUnit) + 1) * ADDITIONAL_FARE);
    }

    public int getFare() {
        return fare;
    }
}
