package wooteco.subway.domain.fare.policy.age;

import wooteco.subway.domain.fare.policy.FarePolicy;

public class BabyDiscountPolicy implements FarePolicy {
    public BabyDiscountPolicy() {
    }

    @Override
    public double calculate(double baseFare) {
        return 0;
    }
}
