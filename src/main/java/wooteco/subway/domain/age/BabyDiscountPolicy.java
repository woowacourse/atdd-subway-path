package wooteco.subway.domain.age;

import wooteco.subway.domain.Fare;

public class BabyDiscountPolicy implements DiscountByAgePolicy {

    private static final int DISCOUNT_PERCENT = 100;

    @Override
    public Fare apply(final Fare fare) {
        return fare.discount(DISCOUNT_PERCENT);
    }
}
