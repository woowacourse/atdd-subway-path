package wooteco.subway.domain.fare;

import wooteco.subway.domain.policy.FarePolicy;

public interface AgeDiscountPolicy extends FarePolicy {

    @Override
    double calculate(double baseFare);
}
