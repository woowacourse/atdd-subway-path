package wooteco.subway.domain.discountpolicy;

public class BasicPolicy implements DiscountPolicy {

    @Override
    public int calculate(int basicFare) {
        return basicFare;
    }
}
