package wooteco.subway.domain.age;

import wooteco.subway.domain.Fare;

public class AdultDiscountPolicy implements DiscountByAgePolicy {

    @Override
    public Fare apply(final Fare fare) {
        return fare;
    }
}
