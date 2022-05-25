package wooteco.subway.domain.farediscount;

public class TeenagerDiscountPolicy extends AgeDiscountPolicy {

    private static final int PERCENT_UNIT = 100;
    public static final int TEENAGER_DISCOUNT_PRICE = 350;
    public static final int TEENAGER_DISCOUNT_PERCENT = 20;

    @Override
    public int apply(int price) {
        price = price - TEENAGER_DISCOUNT_PRICE;
        return price - percentageCalculate(price);
    }

    private int percentageCalculate(int price) {
        return (price / PERCENT_UNIT) * TEENAGER_DISCOUNT_PERCENT;
    }
}
