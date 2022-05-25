package wooteco.subway.domain.fare.strategy.discount;

public final class ChildDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new ChildDiscountStrategy();
    private static final double DISCOUNT_RATE = 0.5;
    private static final int MIN_AGE = 6;
    private static final int MAX_AGE = 12;

    private ChildDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    boolean isMatch(final int age) {
        return MIN_AGE <= age && age <= MAX_AGE;
    }

    @Override
    public int apply(final int fare) {
        return discountFareBy(fare, DISCOUNT_RATE);
    }
}
