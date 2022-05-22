package wooteco.subway.domain.fare;

public class TeenagerDiscountPolicy implements FareDiscountPolicy {

    private static final int BASE_DISCOUNT_AMOUNT = 350;

    @Override
    public int calculateDiscountAmount(final int amount) {
        if (amount < FarePolicy.BASE_FARE) {
            return 0;
        }
        return (int) ((amount - BASE_DISCOUNT_AMOUNT) * 0.2);
    }
}
