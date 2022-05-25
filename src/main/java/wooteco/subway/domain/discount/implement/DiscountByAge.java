package wooteco.subway.domain.discount.implement;

import wooteco.subway.domain.DiscountSpecification;
import wooteco.subway.domain.discount.DiscountStrategy;

public class DiscountByAge implements DiscountStrategy {

    @Override
    public int discount(DiscountSpecification specification) {
        return DiscountStrategyByAge.getDiscountFare(specification.getAge(), specification.getFare());
    }
}
