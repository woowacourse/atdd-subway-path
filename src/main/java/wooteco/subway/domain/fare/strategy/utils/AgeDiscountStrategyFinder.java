package wooteco.subway.domain.fare.strategy.utils;

import wooteco.subway.domain.fare.strategy.discount.AllDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.ChildrenDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.DiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.NoDiscountStrategy;
import wooteco.subway.domain.fare.strategy.discount.TeenagerDiscountStrategy;

public class AgeDiscountStrategyFinder {

    public static final int MIN_CHILDREN_AGE = 6;
    public static final int MIN_TEENAGER_AGE = 13;
    public static final int MAX_TEENAGER_AGE = 19;

    private AgeDiscountStrategyFinder() {
    }

    public static DiscountStrategy findStrategy(int age) {
        if (age >= 0 && age < MIN_CHILDREN_AGE) {
            return new AllDiscountStrategy();
        }
        if (age >= MIN_CHILDREN_AGE && age < MIN_TEENAGER_AGE) {
            return new ChildrenDiscountStrategy();
        }
        if (age >= MIN_TEENAGER_AGE && age < MAX_TEENAGER_AGE) {
            return new TeenagerDiscountStrategy();
        }
        return new NoDiscountStrategy();
    }
}
