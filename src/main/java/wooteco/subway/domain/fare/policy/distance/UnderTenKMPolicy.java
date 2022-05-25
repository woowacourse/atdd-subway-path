package wooteco.subway.domain.fare.policy.distance;

public class UnderTenKMPolicy implements BasePolicy {
    protected static final int BASE_FEE = 1250;

    public UnderTenKMPolicy(int distance) {
    }

    @Override
    public int getFare() {
        return BASE_FEE;
    }
}
