package wooteco.subway.domain.fare.strategy.discount;

public final class BabyDiscountStrategy extends DiscountStrategy {

    private static final DiscountStrategy INSTANCE = new BabyDiscountStrategy();
    private static final int MIN_AGE = 1;
    private static final int MAX_AGE = 5;

    private BabyDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isMatch(final int age) {
        return MIN_AGE <= age && age <= MAX_AGE;
    }

    @Override
    public int calculate(final int fare) {
        return FREE;
    }
}
