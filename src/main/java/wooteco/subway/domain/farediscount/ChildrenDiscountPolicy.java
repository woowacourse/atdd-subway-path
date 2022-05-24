package wooteco.subway.domain.farediscount;

public class ChildrenDiscountPolicy extends AgeDiscountPolicy{

    @Override
    public int apply(int price) {
        price = price - 350;
        return price - percentageCalculate(price);
    }

    private int percentageCalculate(int price) {
        return (price / 100) * 50;
    }
}
