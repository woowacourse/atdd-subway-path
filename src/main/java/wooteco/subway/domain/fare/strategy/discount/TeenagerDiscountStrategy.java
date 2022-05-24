package wooteco.subway.domain.fare.strategy.discount;

public final class TeenagerDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new TeenagerDiscountStrategy();
    private static final double DISCOUNT_RATE = 0.8;

    private TeenagerDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return discountFareBy(fare, DISCOUNT_RATE);
    }
}
