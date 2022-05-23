package wooteco.subway.domain.fare.policy.distance;

import wooteco.subway.domain.fare.DistancePolicy;

public class UnderTenKMPolicy implements DistancePolicy {
    static final int BASE_FEE = 1250;

    @Override
    public int getFare(int distance) {
        return BASE_FEE;
    }
}
