package wooteco.subway.domain.fare.strategy.discount;

public abstract class DiscountStrategy {

    protected static final int FREE = 0;
    private static final int DEFAULT_DISCOUNT_AMOUNT = 350;

    public abstract int calculate(final int fare);

    protected int discountFareBy(final int fare, final double discountRate) {
        return (int) ((fare - DEFAULT_DISCOUNT_AMOUNT) * discountRate);
    }
}
