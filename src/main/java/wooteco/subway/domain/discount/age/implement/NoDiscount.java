package wooteco.subway.domain.discount.age.implement;

import wooteco.subway.domain.Fare;
import wooteco.subway.domain.discount.age.AgeDiscountStrategy;

public class NoDiscount implements AgeDiscountStrategy {

    @Override
    public Fare discount(Fare fare) {
        return fare;
    }
}
