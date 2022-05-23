package wooteco.subway.domain.policy;

import wooteco.subway.domain.fare.Fare;

public interface DiscountPolicy {

    int calculateDiscountFare(Fare fare);
}
