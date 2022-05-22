package wooteco.subway.domain.policy.distance;

import wooteco.subway.domain.fare.DistancePolicy;

public class OverFiftyKMPolicy implements DistancePolicy {
    static final int BASE_FEE = 2050;
    private static final int OVER_DISTANCE = 50;
    private static final double OVER_RATE = 8;

    private final int distance;

    public OverFiftyKMPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int getFare() {
        return BASE_FEE + (int) (Math.ceil((distance - OVER_DISTANCE) / OVER_RATE) * 100);
    }
}
