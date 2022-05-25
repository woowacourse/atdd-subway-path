package wooteco.subway.domain.strategy.fare.age;

import static wooteco.subway.domain.strategy.fare.age.FareDiscountAgeConstant.DEDUCT_FARE;

public class TeenAgeStrategy implements FareDiscountAgeStrategy {

    private static final double TEEN_AGE_DISCOUNT_PERCENT = 0.2;

    @Override
    public boolean isApplied(int age) {
        return age >= 13 && age <= 19;
    }

    @Override
    public int calculateDiscount(int totalAmount) {
        return ((int) (Math.ceil(totalAmount - DEDUCT_FARE) * TEEN_AGE_DISCOUNT_PERCENT) / 10 * 10);
    }
}
