package wooteco.subway.domain.policy;

import wooteco.subway.domain.fare.Fare;

public class AdultPolicy implements DiscountPolicy {

    @Override
    public int calculateDiscountFare(Fare fare) {
        return fare.calculateFare();
    }
}
