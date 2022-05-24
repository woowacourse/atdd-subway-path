package wooteco.subway.domain.discount.age;

import wooteco.subway.domain.discount.DiscountPolicy;

public interface AgeDiscountPolicy extends DiscountPolicy {
    int DEDUCTIBLE = 350;
    double CHILDREN_DISCOUNT_RATE = 0.5;
    double TEENAGER_DISCOUNT_RATE = 0.2;
    int CHILDREN_START_AGE = 6;
    int TEENAGER_START_AGE = 13;
    int ADULT_START_AGE = 19;
}
