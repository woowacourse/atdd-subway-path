package wooteco.subway.domain.path.fare.policy;

public class DefaultDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
