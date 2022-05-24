package wooteco.subway.domain.fare.distanceStrategy;

public class FreeDiscountPolicy implements DistanceDiscountPolicy {
    @Override
    public int calculateFare(int distance, double fare) {
        return (int)fare;
    }
}
