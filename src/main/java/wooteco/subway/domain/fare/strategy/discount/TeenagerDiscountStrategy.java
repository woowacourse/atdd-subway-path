package wooteco.subway.domain.fare.strategy.discount;

public class TeenagerDiscountStrategy implements DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new TeenagerDiscountStrategy();

    private static final int DEFAULT_DISCOUNT_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.8;

    private TeenagerDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return (int) ((fare - DEFAULT_DISCOUNT_AMOUNT) * DISCOUNT_RATE);
    }
}
