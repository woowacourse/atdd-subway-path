package wooteco.subway.domain.age;

import wooteco.subway.domain.Fare;

public class BabyDiscountPolicy implements DiscountByAgePolicy {

    @Override
    public Fare apply(final Fare fare) {
        return new Fare(0);
    }
}
