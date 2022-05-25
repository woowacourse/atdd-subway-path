package wooteco.subway.domain;

import wooteco.subway.domain.discountpolicy.DiscountPolicy;
import wooteco.subway.domain.farepolicy.FarePolicy;

public class Fare {

    private final FarePolicy farePolicy;
    private final DiscountPolicy discountPolicy;

    public Fare(FarePolicy farePolicy, DiscountPolicy discountPolicy) {
        this.farePolicy = farePolicy;
        this.discountPolicy = discountPolicy;
    }

    public int calculate(int distance, int extraFare) {
        return discountPolicy.calculate(farePolicy.calculate(distance) + extraFare);
    }
}
