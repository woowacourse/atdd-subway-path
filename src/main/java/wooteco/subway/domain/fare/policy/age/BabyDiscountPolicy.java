package wooteco.subway.domain.fare.policy.age;

import wooteco.subway.domain.fare.AgeDiscountPolicy;

public class BabyDiscountPolicy implements AgeDiscountPolicy {
    public BabyDiscountPolicy() {
    }

    @Override
    public double calculate(double baseFare) {
        return 0;
    }
}
