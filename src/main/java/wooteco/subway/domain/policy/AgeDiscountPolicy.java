package wooteco.subway.domain.policy;

import wooteco.subway.domain.fare.FarePolicy;

public interface AgeDiscountPolicy extends FarePolicy {

    @Override
    double calculate(double baseFare);
}
