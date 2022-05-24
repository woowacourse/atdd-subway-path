package wooteco.subway.domain.fare.policy.distance;

public class UnderTenKMPolicy implements DistancePolicy {
    protected static final int BASE_FEE = 1250;

    @Override
    public int getFare(int distance) {
        return BASE_FEE;
    }
}
