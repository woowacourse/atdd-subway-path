package wooteco.subway.domain;

public class Fare {
    private static final int MINIMUM_BOUNDARY = 10;
    private static final int MAXIMUM_BOUNDARY = 50;
    private static final int OVER_TEN_POLICY = 5;
    private static final int OVER_FIFTY_POLICY = 8;
    private static final int SURCHARGE = 100;

    private final int baseFare;

    public Fare(int baseFare) {
        this.baseFare = baseFare;
    }

    public int calculateFare(int distance) {
        int fare = baseFare;

        fare = belowMaximumBoundary(fare, distance);
        fare = overMaximumBoundary(fare, distance);

        return fare;
    }

    private int belowMaximumBoundary(int fare, int distance) {
        if (MINIMUM_BOUNDARY < distance && distance <= MAXIMUM_BOUNDARY) {
            fare += calculateOverFare(distance - MINIMUM_BOUNDARY, OVER_TEN_POLICY);
        }
        return fare;
    }

    private int overMaximumBoundary(int fare, int distance) {
        if (MAXIMUM_BOUNDARY < distance) {
            fare += calculateOverFare(MAXIMUM_BOUNDARY - MINIMUM_BOUNDARY, OVER_TEN_POLICY);
            fare += calculateOverFare(distance - MAXIMUM_BOUNDARY, OVER_FIFTY_POLICY);
        }
        return fare;
    }

    private int calculateOverFare(int overDistance, int policy) {
        return (int)((Math.ceil((overDistance - 1) / policy) + 1) * SURCHARGE);
    }
}
