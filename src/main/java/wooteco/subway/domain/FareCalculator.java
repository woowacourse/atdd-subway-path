package wooteco.subway.domain;

public class FareCalculator {

    private final double distance;

    public FareCalculator(final double distance) {
        this.distance = distance;
    }

    public int calculateFare(final int age) {
        int distanceFare = FareByDistance.findFare(distance);
        return distanceFare;
    }

}
