package wooteco.subway.domain.path.fare.policy;

public class TeenagerDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double TEENAGER_RATE = 0.8;

    @Override
    public int calculate(int fare) {
        return (int) ((fare - DEDUCTIBLE_AMOUNT) * TEENAGER_RATE);
    }
}
