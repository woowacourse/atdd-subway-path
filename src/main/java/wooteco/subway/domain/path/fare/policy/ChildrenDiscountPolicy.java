package wooteco.subway.domain.path.fare.policy;

public class ChildrenDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double CHILDREN_RATE = 0.5;

    @Override
    public int calculate(int fare) {
        return (int) ((fare - DEDUCTIBLE_AMOUNT) * CHILDREN_RATE);
    }
}
