package wooteco.subway.domain.fare;

public class FareCalculator {

    private final double distance;

    public FareCalculator(final double distance) {
        this.distance = distance;
    }

    public int calculateFare(final int age, int maxExtraFare) {
        int distanceFare = FareByDistance.findFare(distance);
        return FareByAge.findFare(age, distanceFare + maxExtraFare);
    }

}
