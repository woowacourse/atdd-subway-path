package wooteco.subway.domain.policy.age;

import wooteco.subway.domain.policy.AgeDiscountPolicy;

public class DefaultDiscountPolicy implements AgeDiscountPolicy {
    public DefaultDiscountPolicy() {
    }

    @Override
    public double calculate(double baseFare) {
        return baseFare;
    }
}
