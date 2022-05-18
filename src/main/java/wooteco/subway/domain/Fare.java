package wooteco.subway.domain;

public class Fare {

    public static final int DEFAULT_FARE = 1250;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int distance) {
        return new Fare(DEFAULT_FARE + calculateOverFare(distance));
    }

    private static int calculateOverFare(int distance) {
        if (distance <= 10) {
            return 0;
        }

        if (distance <= 50) {
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
