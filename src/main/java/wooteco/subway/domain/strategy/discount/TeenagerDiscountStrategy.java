package wooteco.subway.domain.strategy.discount;

public class TeenagerDiscountStrategy implements DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new TeenagerDiscountStrategy();

    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.8;

    private TeenagerDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculateDiscountedAmount(final int fare) {
        return (int) ((fare - DEDUCTIBLE_AMOUNT) * DISCOUNT_RATE);
    }
}
