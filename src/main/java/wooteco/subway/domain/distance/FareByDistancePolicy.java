package wooteco.subway.domain.distance;

public class FareByDistancePolicy {

    private static final int DISTANCE_OF_BASIC_FARE = 10;
    private static final int BASIC_FARE = 1250;
    private static final int DISTANCE_OF_OVER_FARE = 50;
    private static final int STANDARD_DISTANCE_OF_OVER_FARE = 5;
    private static final int MAX_STANDARD_DISTANCE_OF_OVER_FARE = 8;
    private static final int STANDARD_OF_OVER_FARE = 100;

    private static int calculateFareByDistance(final Distance distance) {
        if (distance.isLessThanOrEqualByValue(DISTANCE_OF_BASIC_FARE)) {
            return BASIC_FARE;
        }

        if (distance.isLessThanOrEqualByValue(DISTANCE_OF_OVER_FARE)) {
            return BASIC_FARE +
                    calculateOverFare(distance.getValue() - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE);
        }

        return BASIC_FARE +
                calculateOverFare(DISTANCE_OF_OVER_FARE - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE) +
                calculateOverFare(distance.getValue() - DISTANCE_OF_OVER_FARE, MAX_STANDARD_DISTANCE_OF_OVER_FARE);
    }

    private static int calculateOverFare(final int value, final int standardValue) {
        return (int) ((Math.ceil((value - 1) / standardValue) + 1) * STANDARD_OF_OVER_FARE);
    }
}
