package wooteco.subway.domain.discountpolicy;

public class PreferentialPolicy implements DiscountPolicy {

    @Override
    public int calculate(int basicFare) {
        return 0;
    }
}
