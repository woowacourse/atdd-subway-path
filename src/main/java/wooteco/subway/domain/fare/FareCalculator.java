package wooteco.subway.domain.fare;

public class FareCalculator {

    private final FareCalculatePolicy fareCalculatePolicy;

    public FareCalculator(FareCalculatePolicy fareCalculatePolicy) {
        this.fareCalculatePolicy = fareCalculatePolicy;
    }
    public int calculate(int distance, int extraFare, int age) {
        return fareCalculatePolicy.calculate(distance, extraFare, age);
    }
}
