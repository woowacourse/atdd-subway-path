package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int INCREASE_FARE = 100;
    private static final int PER_DISTANCE_OVER_TEN = 5;
    private static final int PER_DISTANCE_OVER_FIFTY = 8;

    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public static Fare from(int distance) {
        return new Fare(distance);
    }

    public int calculate() {
        return BASIC_FARE + getOverTenFare() + getOverFiftyFare();
    }

    private int getOverTenFare() {
        if (distance > 10) {
            return Math.min(800, (int)(Math.ceil((distance - 10) / PER_DISTANCE_OVER_TEN) + 1) * INCREASE_FARE);
        }
        return 0;
    }

    private int getOverFiftyFare() {
        if (distance > 50) {
            return (int)(Math.ceil((distance - 50) / PER_DISTANCE_OVER_FIFTY)) * INCREASE_FARE;
        }
        return 0;
    }
}
