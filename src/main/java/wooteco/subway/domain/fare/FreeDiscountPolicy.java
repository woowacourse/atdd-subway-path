package wooteco.subway.domain.fare;

public class FreeDiscountPolicy implements DiscountPolicy {

    @Override
    public int calculate(int fare) {
        return 0;
    }
}
