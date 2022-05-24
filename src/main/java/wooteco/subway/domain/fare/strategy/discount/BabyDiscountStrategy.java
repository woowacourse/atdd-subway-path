package wooteco.subway.domain.fare.strategy.discount;

public final class BabyDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new BabyDiscountStrategy();

    private BabyDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return FREE;
    }
}
