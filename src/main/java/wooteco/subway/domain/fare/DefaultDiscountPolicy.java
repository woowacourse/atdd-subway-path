package wooteco.subway.domain.fare;

public class DefaultDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
