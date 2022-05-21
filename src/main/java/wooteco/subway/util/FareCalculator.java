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

    public static int calculate(final double distance) {
        return MINIMUM_FARE + addOverTen(distance) + addOverFifty(distance);
    }

    private static int addOverTen(double distance) {
        if (isUnderAdditionalFareDistance(distance, FIRST_ADDITIONAL_FARE_DISTANCE)) {
            return 0;
        }
        if (distance >= SECOND_ADDITIONAL_FARE_DISTANCE) {
            distance = SECOND_ADDITIONAL_FARE_DISTANCE;
        }
        int portion = getPortion(distance, FIRST_ADDITIONAL_FARE_DISTANCE, FIRST_ADDITIONAL_STANDARD_DISTANCE);
        return calculateFare(portion);
    }

    private static int addOverFifty(final double distance) {
        if (isUnderAdditionalFareDistance(distance, SECOND_ADDITIONAL_FARE_DISTANCE)) {
            return 0;
        }
        int portion = getPortion(distance, SECOND_ADDITIONAL_FARE_DISTANCE, SECOND_ADDITIONAL_STANDARD_DISTANCE);
        return calculateFare(portion);
    }

    private static boolean isUnderAdditionalFareDistance(final double distance, final int additionalFareDistance) {
        return distance <= additionalFareDistance;
    }

    private static int getPortion(final double distance, final int additionalFareDistance, final int additionalStandardDistance) {
        int portion = (int) (distance - additionalFareDistance) / additionalStandardDistance;
        if (portion == 0) {
            portion = 1;
        }
        return portion;
    }

    private static int calculateFare(final int portion) {
        return portion * MINIMUM_ADDITIONAL_FARE;
    }
}