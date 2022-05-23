package wooteco.subway.domain.fare.policy.age;

import wooteco.subway.domain.fare.policy.FarePolicy;

public interface AgeDiscountPolicy extends FarePolicy {

    @Override
    double calculate(double baseFare);
}
