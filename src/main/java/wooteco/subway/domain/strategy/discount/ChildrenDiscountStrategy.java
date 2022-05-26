package wooteco.subway.domain.strategy.discount;

public class ChildrenDiscountStrategy implements FareDiscountStrategy {

    private static final int NOT_DISCOUNT_FARE = 350;
    private static final double DISCOUNT_RATE = 0.5;

    @Override
    public int calculateDiscount(int fare) {
        return (int) ((fare - NOT_DISCOUNT_FARE) * DISCOUNT_RATE);
    }

}
