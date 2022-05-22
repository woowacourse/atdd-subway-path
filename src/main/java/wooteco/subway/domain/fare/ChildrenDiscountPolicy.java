package wooteco.subway.domain.fare;

public class ChildrenDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double DISCOUNT = 0.5;

    @Override
    public int calculate(int fare) {
        return (int) ((fare - DEDUCTIBLE_AMOUNT) * DISCOUNT);
    }
}
