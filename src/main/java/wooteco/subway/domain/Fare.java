package wooteco.subway.domain;

public class Fare {
    private static final int MINIMUM_FARE = 1250;
    private static final int EXTRA_FARE = 100;
    private static final int EXTRA_DISTANCE_OVER_50KM = 8;
    private static final int EXTRA_DISTANCE_OVER_10KM = 5;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(double distance) {
        int fare = MINIMUM_FARE + calculateOver50km(distance) + calculateOver10kmUnder50km(distance);
        return new Fare(fare);
    }

    private static int calculateOver50km(double distance) {
        double extraDistance = distance - 50;
        if (extraDistance <= 0) {
            return 0;
        }
        return (int)((Math.ceil(extraDistance / EXTRA_DISTANCE_OVER_50KM)) * EXTRA_FARE) + calculateOver10kmUnder50km(
                50);
    }

    private static int calculateOver10kmUnder50km(double distance) {
        double extraDistance = distance - 10;
        if (extraDistance <= 0 || extraDistance > 40) {
            return 0;
        }
        return (int)((Math.ceil(extraDistance / EXTRA_DISTANCE_OVER_10KM)) * EXTRA_FARE);
    }

    public int getValue() {
        return value;
    }
}
