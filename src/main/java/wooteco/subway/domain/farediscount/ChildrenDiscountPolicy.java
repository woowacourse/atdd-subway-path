package wooteco.subway.domain.farediscount;

public class ChildrenDiscountPolicy extends AgeDiscountPolicy{

    private static final int CHILDREN_DISCOUNT_PRICE = 350;
    private static final int CHILDREN_DISCOUNT_PERCENT = 50;
    private static final int PERCENT_UNIT = 100;

    @Override
    public int apply(int price) {
        price = price - CHILDREN_DISCOUNT_PRICE;
        return price - percentageCalculate(price);
    }

    private int percentageCalculate(int price) {
        return (price / PERCENT_UNIT) * CHILDREN_DISCOUNT_PERCENT;
    }
}
