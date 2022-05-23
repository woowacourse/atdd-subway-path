package wooteco.subway.domain.strategy.discount;

public class DefaultDiscountStrategy implements DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new DefaultDiscountStrategy();

    private DefaultDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculateDiscountedAmount(final int fare) {
        return fare;
    }
}
