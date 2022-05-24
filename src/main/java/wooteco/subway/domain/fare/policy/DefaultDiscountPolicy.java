package wooteco.subway.domain.fare.policy;

public class DefaultDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
