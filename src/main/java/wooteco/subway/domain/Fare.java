package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;

    private final int fare;

    public Fare(int extraFare) {
        this.fare = BASIC_FARE + extraFare;
    }

    public int calculateFare(int distance, Age age) {
        int fareByDistance = getFareByDistance(distance);
        return age.calculateDiscount(fareByDistance);
    }

    private int getFareByDistance(int distance) {
        DistanceArea calculatedDistanceArea = DistanceArea.findDistance(distance);
        return fare + (calculatedDistanceArea.calculateFare(distance) * ADDITIONAL_FARE);
    }
}
