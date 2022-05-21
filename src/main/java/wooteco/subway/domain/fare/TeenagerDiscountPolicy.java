package wooteco.subway.domain.fare;

public class TeenagerDiscountPolicy implements FareDiscountPolicy {

    private static final int BASE_DISCOUNT_AMOUNT = 350;

    @Override
    public int calculateDiscountAmount(final int amount) {
        if (amount <= BASE_DISCOUNT_AMOUNT) {
            return amount;
        }
        return (int) ((amount - BASE_DISCOUNT_AMOUNT) * 0.2) + BASE_DISCOUNT_AMOUNT;
    }
}