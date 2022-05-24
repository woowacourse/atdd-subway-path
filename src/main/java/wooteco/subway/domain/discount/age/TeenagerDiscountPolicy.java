package wooteco.subway.domain.discount.age;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.discount.DiscountCondition;

@Component
@Order(1)
public class TeenagerDiscountPolicy implements AgeDiscountPolicy {
    @Override
    public int applyDiscount(final int money) {
        return (int) (((money - DEDUCTIBLE) * TEENAGER_FARE_RATE));
    }

    @Override
    public boolean accept(final DiscountCondition discountCondition) {
        return discountCondition.getAge() >= TEENAGER_START_AGE && discountCondition.getAge() < ADULT_START_AGE;
    }
}
