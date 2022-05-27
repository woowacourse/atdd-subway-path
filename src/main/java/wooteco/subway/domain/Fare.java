package wooteco.subway.domain;

public class Fare {
    private static final int MINIMUM_FARE = 1250;
    private static final int EXTRA_FARE = 100;
    private static final int EXTRA_DISTANCE_OVER_50KM = 8;
    private static final int EXTRA_DISTANCE_OVER_10KM = 5;
    private static final int LONG_DISTANCE = 50;
    private static final int SHORT_DISTANCE = 10;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(double distance, int extraFare, int age) {
        int fare = MINIMUM_FARE + calculateOver50km(distance) + calculateOver10kmUnder50km(distance) + extraFare;
        return new Fare(DiscountFareCalculator.gerFare(age, fare));
    }

    private static int calculateOver50km(double distance) {
        double extraDistance = distance - LONG_DISTANCE;
        if (extraDistance <= 0) {
            return 0;
        }
        return (int)((Math.ceil(extraDistance / EXTRA_DISTANCE_OVER_50KM)) * EXTRA_FARE) + calculateOver10kmUnder50km(
                LONG_DISTANCE);
    }

    private static int calculateOver10kmUnder50km(double distance) {
        double extraDistance = distance - SHORT_DISTANCE;
        if (extraDistance <= 0 || extraDistance > 40) {
            return 0;
        }
        return (int)((Math.ceil(extraDistance / EXTRA_DISTANCE_OVER_10KM)) * EXTRA_FARE);
    }

    public int getValue() {
        return value;
    }
}
