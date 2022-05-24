package wooteco.subway.domain.fare.policy.distance;

public class TenToFiftyKMPolicy implements BasePolicy {
    private static final int BASE_FEE = UnderTenKMPolicy.BASE_FEE;
    private static final int OVER_TEN_DISTANCE = 10;
    private static final double OVER_TEN_RATE = 5;

    private final int distance;

    public TenToFiftyKMPolicy(int distance) {
        this.distance = distance;
    }


    @Override
    public int getFare() {
        return BASE_FEE + (int) (Math.ceil((distance - OVER_TEN_DISTANCE) / OVER_TEN_RATE) * 100);
    }
}
