package wooteco.subway.domain.fare.policy.age;

import wooteco.subway.domain.fare.policy.FarePolicy;

public class AdultDiscountPolicy implements FarePolicy {
    @Override
    public double calculate(double baseFare) {
        return baseFare;
    }
}
