package wooteco.subway.domain.discountpolicy;

public class AdultDiscountPolicy implements AgeDiscountPolicy {

    @Override
    public int discount(final int fare) {
        return fare;
    }
}
