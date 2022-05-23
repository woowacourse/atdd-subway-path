package wooteco.subway.domain;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_OVER_FARE_DISTANCE_THRESHOLD = 10;
    private static final int SECOND_OVER_FARE_DISTANCE_THRESHOLD = 50;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int distance, int extraFare) {
        return new Fare(DEFAULT_FARE + calculateOverFare(distance) + extraFare);
    }

    private static int calculateOverFare(int distance) {
        if (distance <= FIRST_OVER_FARE_DISTANCE_THRESHOLD) {
            return 0;
        }

        if (distance <= SECOND_OVER_FARE_DISTANCE_THRESHOLD) {
            distance -= 10;
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }

        distance -= 50;
        return 800 + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
