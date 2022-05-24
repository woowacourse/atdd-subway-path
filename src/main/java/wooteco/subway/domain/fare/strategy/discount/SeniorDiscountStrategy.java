package wooteco.subway.domain.fare.strategy.discount;

public class SeniorDiscountStrategy implements DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new SeniorDiscountStrategy();

    private SeniorDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final int fare) {
        return 0;
    }
}
