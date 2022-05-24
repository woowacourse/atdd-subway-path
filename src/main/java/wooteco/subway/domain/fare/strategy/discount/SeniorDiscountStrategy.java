package wooteco.subway.domain.fare.strategy.discount;

public final class SeniorDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new SeniorDiscountStrategy();
    private static final int MIN_AGE = 65;

    private SeniorDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    boolean isMatch(final int age) {
        return MIN_AGE <= age;
    }

    @Override
    public int apply(final int fare) {
        return FREE;
    }
}
