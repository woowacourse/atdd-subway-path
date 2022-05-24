package wooteco.subway.domain.fare.strategy.discount;

public class TeenagerDiscountStrategy implements DiscountStrategy {

    private static final int DEFAULT_DISCOUNT = 350;
    private static final double DISCOUNT_RATE = 0.8;

    @Override
    public int getDiscountedFare(int fare) {
        return (int) ((fare - DEFAULT_DISCOUNT) * DISCOUNT_RATE);
    }
}
