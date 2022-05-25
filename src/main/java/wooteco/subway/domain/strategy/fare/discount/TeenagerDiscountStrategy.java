package wooteco.subway.domain.strategy.fare.discount;

public class TeenagerDiscountStrategy implements DiscountStrategy {

    private static final int DEDUCT_MONEY = 350;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final int TEENAGER_AGE_MIN = 13;
    private static final int TEENAGER_AGE_MAX = 18;

    @Override
    public int calculateDiscount(int price) {
        return (int) ((price - DEDUCT_MONEY) * TEENAGER_DISCOUNT_RATE);
    }

    @Override
    public boolean isUsable(int age) {
        return TEENAGER_AGE_MIN <= age && age <= TEENAGER_AGE_MAX;
    }
}
