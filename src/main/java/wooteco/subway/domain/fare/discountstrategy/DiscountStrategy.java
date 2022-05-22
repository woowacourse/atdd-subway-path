package wooteco.subway.domain.fare.discountstrategy;

import wooteco.subway.domain.fare.Fare;

@FunctionalInterface
public interface DiscountStrategy {
    Fare discount(Fare fare);
}
