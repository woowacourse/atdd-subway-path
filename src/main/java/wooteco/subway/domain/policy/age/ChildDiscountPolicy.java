package wooteco.subway.domain.policy.age;

import wooteco.subway.domain.policy.AgeDiscountPolicy;

public class ChildDiscountPolicy implements AgeDiscountPolicy {
    private static final int DEDUCTION_FARE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    public ChildDiscountPolicy() {
    }

    @Override
    public double calculate(double baseFare) {
        return (baseFare - DEDUCTION_FARE) * CHILD_DISCOUNT_RATE;
    }
}
