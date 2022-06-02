package wooteco.subway.domain.discount;

import wooteco.subway.domain.DiscountSpecification;
import wooteco.subway.domain.Fare;

public interface DiscountStrategy {

    Fare discount(DiscountSpecification specification);
}
