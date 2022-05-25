package wooteco.subway.domain.fare;

public class ChildDiscountStrategy implements DiscountStrategy {

    private final static int BASE_DISCOUNT_AMOUNT = 350;
    private final static double CHILD_DISCOUNT_PERCENTAGE = 0.5;

    private static final DiscountStrategy INSTANCE = new ChildDiscountStrategy();

    private ChildDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int discount(int fare) {
        return (int) ((fare - BASE_DISCOUNT_AMOUNT) * CHILD_DISCOUNT_PERCENTAGE);
    }
}
