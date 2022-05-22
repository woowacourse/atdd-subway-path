package wooteco.subway.domain.policy.age;

import wooteco.subway.domain.policy.AgeDiscountPolicy;

public class BabyDiscountPolicy implements AgeDiscountPolicy {
    public BabyDiscountPolicy() {
    }

    @Override
    public double calculate(double baseFare) {
        return 0;
    }
}
