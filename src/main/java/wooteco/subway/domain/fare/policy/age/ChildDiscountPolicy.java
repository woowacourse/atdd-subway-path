package wooteco.subway.domain.fare.policy.age;

import wooteco.subway.domain.fare.policy.FarePolicy;

public class ChildDiscountPolicy implements FarePolicy {
    private static final int DEDUCTION_FARE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    @Override
    public double calculate(double baseFare) {
        return (baseFare - DEDUCTION_FARE) * CHILD_DISCOUNT_RATE;
    }
}
