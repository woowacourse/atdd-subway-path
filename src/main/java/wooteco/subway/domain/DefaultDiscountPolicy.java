package wooteco.subway.domain;

public class DefaultDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
