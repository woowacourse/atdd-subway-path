package wooteco.subway.domain.farediscount;

public class FreeDiscountPolicy extends AgeDiscountPolicy {

    @Override
    public int apply(int price) {
        return 0;
    }
}
