package wooteco.subway.domain;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;
    private static final int EXCLUDED_FARE = 350;
    private static final int DISTANCE_1 = 10;
    private static final int DISTANCE_2 = 50;
    private static final int UNIT_1 = 5;
    private static final int UNIT_2 = 8;
    private static final int CHILDREN_AGE = 13;
    private static final int CHILDREN_DISCOUNT_RATE = 50;
    private static final int YOUTH_AGE = 19;
    private static final int YOUTH_DISCOUNT_RATE = 20;

    private final int distance;
    private final int extraFare;
    private final int age;

    public FareCalculator(int distance, int extraFare, int age) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = age;
    }

    public int calculate() {
        return calculateByAge(calculateByDistance() + extraFare);
    }

    private int calculateByAge(int fare) {
        if (age < CHILDREN_AGE) {
            fare = calculateFareByDiscountRate(fare, CHILDREN_DISCOUNT_RATE);
            return floor(fare);
        }
        if (age < YOUTH_AGE) {
            fare = calculateFareByDiscountRate(fare, YOUTH_DISCOUNT_RATE);
            return floor(fare);
        }
        return fare;
    }

    private int calculateFareByDiscountRate(int fare, int discountRate) {
        return fare - (fare - EXCLUDED_FARE) * discountRate / 100;
    }

    private int floor(int number) {
        return number - number % 10;
    }

    private int calculateByDistance() {
        int fare = BASIC_FARE;
        int distance = this.distance;

        if (distance > DISTANCE_2) {
            fare += calculateFare(distance, DISTANCE_2, UNIT_2);
            distance = DISTANCE_2;
        }
        if (distance > DISTANCE_1) {
            fare += calculateFare(distance, DISTANCE_1, UNIT_1);
        }
        return fare;
    }

    private int calculateFare(int distance, int paidDistance, int unit) {
        return ((distance - paidDistance - 1) / unit + 1) * EXTRA_FARE;
    }
}
