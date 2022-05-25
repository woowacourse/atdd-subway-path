package wooteco.subway.domain.age;

import wooteco.subway.domain.Fare;

public class TeenagerDiscountPolicy implements DiscountByAgePolicy {

    @Override
    public Fare apply(final Fare fare) {
        return fare.minus(350).discount(0.8);
    }
}
