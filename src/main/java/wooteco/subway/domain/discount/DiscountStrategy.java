package wooteco.subway.domain.discount;

import wooteco.subway.domain.DiscountSpecification;

public interface DiscountStrategy {

    int discount(DiscountSpecification specification);
}
