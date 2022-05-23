package wooteco.subway.domain.fare.distanceStrategy;

public class ExtraDiscountPolicy extends Overed implements DistanceDiscountPolicy {
    private static final int MAXIMUM_BOUNDARY = 50;
    private static final int OVER_FIFTY_POLICY = 8;
    private static final int MINIMUM_BOUNDARY = 10;
    private static final int OVER_TEN_POLICY = 5;

    @Override
    public int calculateFare(int distance, double fare) {
        return (int)(fare
            + calculateOverFare(MAXIMUM_BOUNDARY - MINIMUM_BOUNDARY, OVER_TEN_POLICY)
            + calculateOverFare(distance - MAXIMUM_BOUNDARY, OVER_FIFTY_POLICY));
    }
}
