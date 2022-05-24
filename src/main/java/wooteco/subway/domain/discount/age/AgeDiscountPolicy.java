package wooteco.subway.domain.discount.age;

import wooteco.subway.domain.discount.DiscountPolicy;

public interface AgeDiscountPolicy extends DiscountPolicy {
    int DEDUCTIBLE = 350;
    double CHILDREN_FARE_RATE = 0.5;
    double TEENAGER_FARE_RATE = 0.8;
    int CHILDREN_START_AGE = 6;
    int TEENAGER_START_AGE = 13;
    int ADULT_START_AGE = 19;
}
