package wooteco.subway.domain.path.fare.policy;

public class FreeDiscountPolicy implements DiscountPolicy {

    private static final int FREE_AMOUNT = 0;

    @Override
    public int calculate(int fare) {
        return FREE_AMOUNT;
    }
}
