package wooteco.subway.domain.policy;

import wooteco.subway.domain.fare.Fare;

public class InfantsPolicy implements DiscountPolicy{

    private static final int FREE_FARE = 0;

    @Override
    public int calculateDiscountFare(Fare fare) {
        return FREE_FARE;
    }
}
