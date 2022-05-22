package wooteco.subway.domain.fare.discountstrategy;

import wooteco.subway.domain.fare.Age;

public class DiscountStrategyFactory {
    private static final Age CHILDREN_MIN_AGE = new Age(6);
    private static final Age TEENAGER_MIN_AGE = new Age(13);
    private static final Age ADULT_MIN_AGE = new Age(19);

    public DiscountStrategy getDiscountStrategy(Age age) {
        if (age.isLessThan(CHILDREN_MIN_AGE)) {
            return new BabyDiscountStrategy();
        }

        if (age.isLessThan(TEENAGER_MIN_AGE)) {
            return new ChildrenDiscountStrategy();
        }

        if (age.isLessThan(ADULT_MIN_AGE)) {
            return new TeenagerDiscountStrategy();
        }

        return new AdultDiscountStrategy();
    }
}
