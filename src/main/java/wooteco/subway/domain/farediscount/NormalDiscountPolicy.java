package wooteco.subway.domain.farediscount;

public class NormalDiscountPolicy extends AgeDiscountPolicy{

    @Override
    public int apply(int price) {
        return price;
    }
}
