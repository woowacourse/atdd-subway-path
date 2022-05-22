package wooteco.subway.domain.fare;

import wooteco.subway.domain.DiscountPolicy;

public class DefaultDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
