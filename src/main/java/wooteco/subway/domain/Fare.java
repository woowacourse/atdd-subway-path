package wooteco.subway.domain;

public class Fare {

    private static final int DISTANCE_OF_BASIC_FARE = 10;
    private static final int BASIC_FARE = 1250;
    private static final int DISTANCE_OF_OVER_FARE = 50;
    private static final int STANDARD_DISTANCE_OF_OVER_FARE = 5;
    private static final int MAX_STANDARD_DISTANCE_OF_OVER_FARE = 8;
    private static final int STANDARD_OF_OVER_FARE = 100;

    private final Distance distance;

    public Fare(final Distance distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance.isLessThanOrEqual(DISTANCE_OF_BASIC_FARE)) {
            return BASIC_FARE;
        }
        if (distance.isLessThanOrEqual(DISTANCE_OF_OVER_FARE)) {
            return BASIC_FARE +
                    calculateOverFare(distance.getValue() - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE);
        }
        return BASIC_FARE +
                calculateOverFare(DISTANCE_OF_OVER_FARE - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE) +
                calculateOverFare(distance.getValue() - DISTANCE_OF_OVER_FARE, MAX_STANDARD_DISTANCE_OF_OVER_FARE);
    }

    private int calculateOverFare(final int distance, final int standardDistance) {
        return (int) ((Math.ceil((distance - 1) / standardDistance) + 1) * STANDARD_OF_OVER_FARE);
    }
}
