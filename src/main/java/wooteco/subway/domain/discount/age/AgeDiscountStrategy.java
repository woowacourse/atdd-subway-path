package wooteco.subway.domain.discount.age;

import wooteco.subway.domain.Fare;

public interface AgeDiscountStrategy {

    Fare discount(Fare fare);
}
