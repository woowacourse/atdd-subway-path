package wooteco.subway.domain.fare;

public class Fare {

    private static final int OVER_FARE = 100;
    private static final int BASIC_FARE = 1250;
    private static final int FIRST_ADDITIONAL_INTERVAL = 10;
    private static final int SECOND_ADDITIONAL_INTERVAL = 50;
    private static final double FIRST_INTERVAL_UNIT = 5.0;
    private static final double SECOND_INTERVAL_UNIT = 8.0;

    private static final int CHILD_MIN_AGE = 6;
    private static final int ADOLESCENT_MIN_AGE = 13;
    private static final int ADULT_MIN_AGE = 19;
    private static final int BASIC_DISCOUNT_CHARGE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double ADOLESCENT_DISCOUNT_RATE = 0.2;

    private final int distance;
    private final int extraCharge;
    private final int age;

    public Fare(int distance, int extraCharge, int age) {
        this.distance = distance;
        this.extraCharge = extraCharge;
        this.age = age;
    }

    public int calculate() {
        if (distance <= FIRST_ADDITIONAL_INTERVAL) {
            return discount(BASIC_FARE + extraCharge);
        }
        if (distance <= SECOND_ADDITIONAL_INTERVAL) {
            return discount(BASIC_FARE + calculateFareOverFirstDistance(distance) + extraCharge);
        }
        return discount(BASIC_FARE
                + calculateFareOverFirstDistance(SECOND_ADDITIONAL_INTERVAL)
                + calculateFareOverSecondDistance()
                + extraCharge);
    }

    private int discount(int totalCharge) {
        if (CHILD_MIN_AGE <= age && age < ADOLESCENT_MIN_AGE) {
            return (int) ((totalCharge - BASIC_DISCOUNT_CHARGE) * (1 - CHILD_DISCOUNT_RATE));
        }
        if (age < ADULT_MIN_AGE) {
            return (int) ((totalCharge - BASIC_DISCOUNT_CHARGE) * (1 - ADOLESCENT_DISCOUNT_RATE));
        }
        return totalCharge;
    }

    private int calculateFareOverFirstDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_ADDITIONAL_INTERVAL) / FIRST_INTERVAL_UNIT) * OVER_FARE);
    }

    private int calculateFareOverSecondDistance() {
        return (int) (Math.ceil((distance - SECOND_ADDITIONAL_INTERVAL) / SECOND_INTERVAL_UNIT) * OVER_FARE);
    }
}
