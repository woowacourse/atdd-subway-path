package wooteco.subway.domain.farediscount;

public class SpecialDiscountPolicy extends AgeDiscountPolicy {

    @Override
    public int apply(int price) {
        return 0;
    }
}
