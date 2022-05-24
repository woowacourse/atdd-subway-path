package wooteco.subway.domain.fare.strategy.discount;

public final class DefaultDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new DefaultDiscountStrategy();
    private static final int MIN_AGE = 19;
    private static final int MAX_AGE = 64;

    private DefaultDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isMatch(final int age) {
        return MIN_AGE <= age && age <= MAX_AGE;
    }

    @Override
    public int apply(final int fare) {
        return fare;
    }
}
