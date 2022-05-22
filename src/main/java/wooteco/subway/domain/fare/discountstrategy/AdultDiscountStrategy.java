package wooteco.subway.domain.fare.discountstrategy;

import wooteco.subway.domain.fare.Fare;

public class AdultDiscountStrategy implements DiscountStrategy {

    @Override
    public Fare discount(Fare fare) {
        return fare;
    }
}
