package wooteco.subway.domain.fare.distanceStrategy;

public class NormalDistanceDiscountPolicy extends Overed implements DistanceDiscountPolicy {
    private static final int MINIMUM_BOUNDARY = 10;
    private static final int OVER_TEN_POLICY = 5;

    @Override
    public int calculateFare(int distance, double fare) {
        return (int)(fare + calculateOverFare(distance - MINIMUM_BOUNDARY, OVER_TEN_POLICY));
    }
}
