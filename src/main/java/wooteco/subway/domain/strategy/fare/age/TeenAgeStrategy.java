package wooteco.subway.domain.strategy.fare.age;

import static wooteco.subway.domain.strategy.fare.FarePolicy.DEDUCT_FARE;

public class TeenAgeStrategy implements FareDiscountAgeStrategy {

    private static final double TEEN_AGE_DISCOUNT_PERCENT = 0.2;

    @Override
    public int discountAge(int totalAmount) {
        return ((int) (Math.ceil(totalAmount - DEDUCT_FARE) * TEEN_AGE_DISCOUNT_PERCENT) / 10 * 10);
    }
}
