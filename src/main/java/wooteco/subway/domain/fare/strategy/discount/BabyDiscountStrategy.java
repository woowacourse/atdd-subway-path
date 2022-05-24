package wooteco.subway.domain.fare.strategy.discount;

public class BabyDiscountStrategy implements DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new BabyDiscountStrategy();

    private BabyDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return 0;
    }
}
