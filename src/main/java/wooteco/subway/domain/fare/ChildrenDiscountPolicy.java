package wooteco.subway.domain.fare;

public class ChildrenDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return (int) ((fare - 350) * 0.5);
    }
}
