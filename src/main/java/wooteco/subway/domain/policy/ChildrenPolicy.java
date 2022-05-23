package wooteco.subway.domain.policy;

import wooteco.subway.domain.fare.Fare;

public class ChildrenPolicy extends NeedCalculateDiscountPolicy {

    public static final double CHILDREN_DISCOUNT_RATE = 0.5;

    @Override
    public int calculateDiscountFare(Fare fare) {
        return (int) ((fare.calculateFare() - BASE_FARE) * CHILDREN_DISCOUNT_RATE);
    }
}
