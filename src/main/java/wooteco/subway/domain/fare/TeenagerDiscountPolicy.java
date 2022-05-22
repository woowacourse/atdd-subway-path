package wooteco.subway.domain.fare;

public class TeenagerDiscountPolicy implements DiscountPolicy {

    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double DISCOUNT = 0.8;

    @Override
    public int calculate(int fare) {
        return (int) ((fare - DEDUCTIBLE_AMOUNT) * DISCOUNT);
    }
}
