package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final double BASIC_THRESHOLD_DISTANCE = 10.0;
    private static final double LONG_RANGE_THRESHOLD_DISTANCE = 50.0;
    private static final int BASIC_DISTANCE_RATE = 5;
    private static final int LONG_RANGE_DISTANCE_RATE = 8;
    private static final int OVER_FARE = 100;
    private static final int ADULT_AGE_LOWEST_THRESHOLD = 20;
    private static final int KID_AGE_HIGHEST_THRESHOLD = 12;
    private static final int DEFAULT_AGE_DISCOUNT_VALUE = 350;
    private static final double ADOLESCENT_DISCOUNT_RATE = 0.2;
    private static final double KID_DISCOUNT_RATE = 0.5;

    private final int value;

    public Fare(int distance, int highestExtraFare, int age) {
        value = calculate(distance, highestExtraFare, age);
    }

    private int calculate(int distance, int highestExtraFare, int age) {
        int adultFare = BASIC_FARE + calculateOverDistanceFare(distance) + highestExtraFare;
        if (age < ADULT_AGE_LOWEST_THRESHOLD) {
            return calculateAgeDiscountFare(adultFare, age);
        }
        return adultFare;
    }

    private int calculateAgeDiscountFare(int fare, int age) {
        if (age > KID_AGE_HIGHEST_THRESHOLD) {
            return (int) ((fare - DEFAULT_AGE_DISCOUNT_VALUE) * (1 - ADOLESCENT_DISCOUNT_RATE));
        }
        return (int) ((fare - DEFAULT_AGE_DISCOUNT_VALUE) * (1 - KID_DISCOUNT_RATE));
    }

    private int calculateOverDistanceFare(int distance) {
        int fare = 0;
        if (distance <= BASIC_THRESHOLD_DISTANCE) {
            return 0;
        }
        if (distance > LONG_RANGE_THRESHOLD_DISTANCE) {
            fare += (int) (Math.ceil((distance - LONG_RANGE_THRESHOLD_DISTANCE) / LONG_RANGE_DISTANCE_RATE)
                    * OVER_FARE);
        }
        fare += (int) (Math.min(Math.ceil((distance - BASIC_THRESHOLD_DISTANCE) / BASIC_DISTANCE_RATE),
                LONG_RANGE_DISTANCE_RATE) * OVER_FARE);
        return fare;
    }

    public int value() {
        return value;
    }
}
