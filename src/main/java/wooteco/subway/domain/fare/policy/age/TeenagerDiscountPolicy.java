package wooteco.subway.domain.fare.policy.age;

import wooteco.subway.domain.fare.policy.FarePolicy;

public class TeenagerDiscountPolicy implements FarePolicy {
    private static final int DEDUCTION_FARE = 350;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    @Override
    public double calculate(double baseFare) {
        return (baseFare - DEDUCTION_FARE) * TEENAGER_DISCOUNT_RATE;
    }
}
