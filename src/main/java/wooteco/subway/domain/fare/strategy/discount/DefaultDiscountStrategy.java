package wooteco.subway.domain.fare.strategy.discount;

public final class DefaultDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new DefaultDiscountStrategy();

    private DefaultDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return fare;
    }
}
