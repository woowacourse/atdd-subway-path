package wooteco.subway.domain.discountpolicy;

public class ChildrenPolicy implements DiscountPolicy {

    private static final double DISCOUNT_RATE = 0.5;

    @Override
    public int calculate(int basicFare) {
        return (int) ((basicFare - DEDUCTED_AMOUNT) * DISCOUNT_RATE);
    }
}
