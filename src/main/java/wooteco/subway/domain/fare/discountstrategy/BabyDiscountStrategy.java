package wooteco.subway.domain.fare.discountstrategy;

import wooteco.subway.domain.fare.Fare;

public class BabyDiscountStrategy implements DiscountStrategy {

    @Override
    public Fare discount(Fare fare) {
        return new Fare(0);
    }
}
