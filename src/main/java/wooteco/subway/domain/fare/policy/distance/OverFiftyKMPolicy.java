package wooteco.subway.domain.fare.policy.distance;

public class OverFiftyKMPolicy implements DistancePolicy {
    static final int BASE_FEE = 2050;
    private static final int OVER_DISTANCE = 50;
    private static final double OVER_RATE = 8;

    @Override
    public int getFare(int distance) {
        return BASE_FEE + (int) (Math.ceil((distance - OVER_DISTANCE) / OVER_RATE) * 100);
    }
}
