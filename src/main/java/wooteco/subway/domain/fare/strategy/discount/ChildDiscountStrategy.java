package wooteco.subway.domain.fare.strategy.discount;

public class ChildDiscountStrategy implements DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new ChildDiscountStrategy();

    private static final int DEFAULT_DISCOUNT_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.5;

    private ChildDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return (int) ((fare - DEFAULT_DISCOUNT_AMOUNT) * DISCOUNT_RATE);
    }
}
