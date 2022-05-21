package wooteco.subway.domain;

public class FarePolicy {
    public static final int BASE_FARE = 1250;
    private static final int SHORT_DISTANCE_UPPER_BOUND = 10;
    private static final int MIDDLE_DISTANCE_UPPER_BOUND = 50;
    private static final int ADDITIONAL_FARE = 100;
    private static final int MIDDLE_DISTANCE_FARE_RATIO = 5;
    private static final int LONG_DISTANCE_FARE_RATIO = 8;

    public static int calculateFare(int distance, int extraFare) {
        if (distance == 0) {
            return 0;
        }
        return BASE_FARE + calculateAdditionalFare(distance) + extraFare;
    }

    private static int calculateAdditionalFare(int distance) {
        if (distance < SHORT_DISTANCE_UPPER_BOUND) {
            return 0;
        }

        if (distance <= MIDDLE_DISTANCE_UPPER_BOUND) {
            int distanceForPayingAdditionalAmount = distance - SHORT_DISTANCE_UPPER_BOUND;
            return middleDistanceUnit(distanceForPayingAdditionalAmount) * ADDITIONAL_FARE;
        }

        int distanceForPayingAdditionalAmount = distance - MIDDLE_DISTANCE_UPPER_BOUND;
        return fullMiddleDistanceAdditionalFare() + longDistanceUnit(distanceForPayingAdditionalAmount);
    }

    private static int middleDistanceUnit(final int distanceForPayingAdditionalAmount) {
        return (int) Math.ceil((distanceForPayingAdditionalAmount - 1) / MIDDLE_DISTANCE_FARE_RATIO) + 1;
    }

    private static int longDistanceUnit(final int distanceForPayingAdditionalAmount) {
        return (int) (Math.ceil((distanceForPayingAdditionalAmount - 1) / LONG_DISTANCE_FARE_RATIO) + 1)
                * ADDITIONAL_FARE;
    }

    private static int fullMiddleDistanceAdditionalFare() {
        return (MIDDLE_DISTANCE_UPPER_BOUND - SHORT_DISTANCE_UPPER_BOUND) / MIDDLE_DISTANCE_FARE_RATIO
                * ADDITIONAL_FARE;
    }
}
