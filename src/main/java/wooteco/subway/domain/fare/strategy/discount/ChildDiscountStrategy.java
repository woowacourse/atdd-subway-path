package wooteco.subway.domain.fare.strategy.discount;

public final class ChildDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new ChildDiscountStrategy();
    private static final double DISCOUNT_RATE = 0.5;

    private ChildDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return discountFareBy(fare, DISCOUNT_RATE);
    }
}
