package wooteco.subway.domain.fare;

public class FareCalculateWithDistanceAndAge implements FareCalculatePolicy{

    private static final int DEFAULT_FARE = 1250;
    private static final int SURCHARGE_PER_UNIT = 100;
    private static final int FIRST_STANDARD = 10;
    private static final int SECOND_STANDARD = 50;
    private static final int FIRST_STANDARD_UNIT = 5;
    private static final int SECOND_STANDARD_UNIT = 8;
    private static final int SURCHARGE_UNIT_COUNT = 8;
    private static final int TEENAGER_MIN = 13;
    private static final int TEENAGER_MAX = 19;
    private static final int CHILD_MIN = 6;
    private static final int CHILD_MAX = 13;
    private static final int DISCOUNT_FARE = 350;
    private static final double TEENAGER_RATE = 0.8;
    private static final double CHILD_RATE = 0.5;

    @Override
    public int calculate(int distance, int extraFare, int age) {
        int fare = calculateByDistance(distance) + extraFare;
        return calculateByAge(fare, age);
    }

    private static int calculateByDistance(int distance) {
        if (distance == 0) {
            return 0;
        }

        if (distance > SECOND_STANDARD) {
            return DEFAULT_FARE + SURCHARGE_PER_UNIT * SURCHARGE_UNIT_COUNT + (int) (
                    ((Math.ceil((distance - SECOND_STANDARD) / (double) SECOND_STANDARD_UNIT))) * SURCHARGE_PER_UNIT);
        }
        if (distance > FIRST_STANDARD) {
            return DEFAULT_FARE + (int) ((Math.ceil((distance - FIRST_STANDARD) / (double) FIRST_STANDARD_UNIT))
                    * SURCHARGE_PER_UNIT);
        }
        return DEFAULT_FARE;
    }

    private static int calculateByAge(int fare, int age) {
        if (age >= TEENAGER_MIN && age < TEENAGER_MAX) {
            return (int) ((fare - DISCOUNT_FARE) * TEENAGER_RATE);
        }
        if (age >= CHILD_MIN && age < CHILD_MAX) {
            return (int) ((fare - DISCOUNT_FARE) * CHILD_RATE);
        }
        return fare;
    }
}
