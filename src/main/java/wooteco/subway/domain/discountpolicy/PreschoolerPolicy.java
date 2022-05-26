package wooteco.subway.domain.discountpolicy;

public class PreschoolerPolicy implements AgeDiscountPolicy {

    private static final int FREE = 0;

    @Override
    public int discount(final int fare) {
        return FREE;
    }
}
