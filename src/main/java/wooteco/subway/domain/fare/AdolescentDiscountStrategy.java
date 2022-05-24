package wooteco.subway.domain.fare;

public class AdolescentDiscountStrategy implements DiscountStrategy {

    private final static int BASE_DISCOUNT_AMOUNT = 350;
    private final static double ADOLESCENT_DISCOUNT_PERCENTAGE = 0.8;

    private static final DiscountStrategy INSTANCE = new AdolescentDiscountStrategy();

    private AdolescentDiscountStrategy() {
    }

    public static DiscountStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int discount(int fare) {
        return (int) ((fare - BASE_DISCOUNT_AMOUNT) * ADOLESCENT_DISCOUNT_PERCENTAGE);
    }
}
