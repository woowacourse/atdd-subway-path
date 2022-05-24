package wooteco.subway.domain;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_OVER_FARE_DISTANCE_THRESHOLD = 10;
    private static final int SECOND_OVER_FARE_DISTANCE_THRESHOLD = 50;
    private static final int FIRST_OVER_FARE_UNIT = 5;
    private static final int SECOND_OVER_FARE_UNIT = 8;
    private static final int INITIAL_OVER_FARE_BY_SECOND_THRESHOLD = 800;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int distance, int extraFare, int age) {
        final int fareWithoutDiscount = DEFAULT_FARE + calculateOverFare(distance) + extraFare;
        return new Fare(DiscountPolicy.calculateDiscountedFare(age, fareWithoutDiscount));
    }

    private static int calculateOverFare(int distance) {
        if (distance <= FIRST_OVER_FARE_DISTANCE_THRESHOLD) {
            return 0;
        }

        if (distance <= SECOND_OVER_FARE_DISTANCE_THRESHOLD) {
            distance -= FIRST_OVER_FARE_DISTANCE_THRESHOLD;
            return (int) ((Math.ceil((distance - 1) / FIRST_OVER_FARE_UNIT) + 1) * 100);
        }

        distance -= SECOND_OVER_FARE_DISTANCE_THRESHOLD;
        return INITIAL_OVER_FARE_BY_SECOND_THRESHOLD + (int) ((Math.ceil((distance - 1) / SECOND_OVER_FARE_UNIT) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
