package wooteco.subway.domain.strategy.fare.discount;

public class ChildDiscountStrategy implements DiscountStrategy {

    private static final int DEDUCT_MONEY = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final int CHILD_AGE_MIN = 6;
    private static final int CHILD_AGE_MAX = 12;


    @Override
    public int calculateDiscount(int price) {
        return (int) ((price - DEDUCT_MONEY) * CHILD_DISCOUNT_RATE);
    }

    @Override
    public boolean isUsable(int age) {
        return CHILD_AGE_MIN <= age && age <= CHILD_AGE_MAX;
    }
}
