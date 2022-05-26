package wooteco.subway.domain.discountpolicy;

public class TeenDiscountPolicy implements AgeDiscountPolicy {

    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public int discount(final int fare) {
        return (int) (fare - fare * DISCOUNT_RATE);
    }
}
