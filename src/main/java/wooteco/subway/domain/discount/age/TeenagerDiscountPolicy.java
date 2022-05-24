package wooteco.subway.domain.discount.age;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.discount.DiscountCondition;

@Component
@Order(1)
public class TeenagerDiscountPolicy implements AgeDiscountPolicy {
    @Override
    public int applyDiscount(final int money) {
        final int discountedMoney = (int) (((money - DEDUCTIBLE) * TEENAGER_DISCOUNT_RATE));
        return money - discountedMoney;
    }

    @Override
    public boolean accept(final DiscountCondition discountCondition) {
        return discountCondition.getAge() >= TEENAGER_START_AGE && discountCondition.getAge() < ADULT_START_AGE;
    }
}
