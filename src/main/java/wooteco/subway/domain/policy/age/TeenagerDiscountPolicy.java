package wooteco.subway.domain.policy.age;

import wooteco.subway.domain.fare.AgeDiscountPolicy;

public class TeenagerDiscountPolicy implements AgeDiscountPolicy {
    private static final int DEDUCTION_FARE = 350;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    public TeenagerDiscountPolicy() {
    }

    @Override
    public double calculate(double baseFare) {
        return (baseFare - DEDUCTION_FARE) * TEENAGER_DISCOUNT_RATE;
    }
}
