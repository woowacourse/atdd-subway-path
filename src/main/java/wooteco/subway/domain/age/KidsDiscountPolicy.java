package wooteco.subway.domain.age;

import wooteco.subway.domain.Fare;

public class KidsDiscountPolicy implements DiscountByAgePolicy {

    private static final int EXCLUDED_AMOUNT = 350;
    private static final double DISCOUNT_PERCENT = 50;

    @Override
    public Fare apply(final Fare fare) {
        return fare.minus(EXCLUDED_AMOUNT).discount(DISCOUNT_PERCENT);
    }
}
