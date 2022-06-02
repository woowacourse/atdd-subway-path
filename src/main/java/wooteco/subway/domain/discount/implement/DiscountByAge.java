package wooteco.subway.domain.discount.implement;

import wooteco.subway.domain.DiscountSpecification;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.discount.DiscountStrategy;
import wooteco.subway.domain.discount.age.AgeDiscountStrategy;
import wooteco.subway.domain.discount.age.DiscountStrategyByAge;

public class DiscountByAge implements DiscountStrategy {

    @Override
    public Fare discount(DiscountSpecification specification) {
        AgeDiscountStrategy strategy = DiscountStrategyByAge
                .getDiscountFare(specification.getAge());

        return strategy.discount(specification.getFare());
    }
}
