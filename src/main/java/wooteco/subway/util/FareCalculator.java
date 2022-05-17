package wooteco.subway.util;

public class FareCalculator {

    private static final int MINIMUM_FARE = 1250;
    private static final int MINIMUM_ADDITIONAL_FARE = 100;

    private static final int FIRST_ADDITIONAL_FARE_DISTANCE = 10;
    private static final int FIRST_ADDITIONAL_STANDARD_DISTANCE = 5;
    private static final int SECOND_ADDITIONAL_FARE_DISTANCE = 50;
    private static final int SECOND_ADDITIONAL_STANDARD_DISTANCE = 8;

    private FareCalculator() {
    }

    public static int calculate(double distance) {
        return MINIMUM_FARE + addOverTen(distance) + addOverFifty(distance);
    }

    private static int addOverFifty(double distance) {
        if (distance <= SECOND_ADDITIONAL_FARE_DISTANCE) {
            return 0;
        }
        int portion = (int) (distance - SECOND_ADDITIONAL_FARE_DISTANCE) / SECOND_ADDITIONAL_STANDARD_DISTANCE;
        return portion * MINIMUM_ADDITIONAL_FARE;
    }

    private static int addOverTen(double distance) {
        if (distance <= FIRST_ADDITIONAL_FARE_DISTANCE) {
            return 0;
        }
        if (distance >= SECOND_ADDITIONAL_FARE_DISTANCE) {
            distance = SECOND_ADDITIONAL_FARE_DISTANCE;
        }
        int portion = (int) (distance - FIRST_ADDITIONAL_FARE_DISTANCE) / FIRST_ADDITIONAL_STANDARD_DISTANCE;
        return portion * MINIMUM_ADDITIONAL_FARE;
    }
}
