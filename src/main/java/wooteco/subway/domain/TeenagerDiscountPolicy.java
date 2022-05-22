package wooteco.subway.domain;

public class TeenagerDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return (int) ((fare - 350) * 0.8);
    }
}
