package wooteco.subway.domain.farediscount;

public class TeenagerDiscountPolicy extends AgeDiscountPolicy {

    @Override
    public int apply(int price) {
        price = price - 350;
        int i = percentageCalculate(price);
        return price - i;
    }

    private int percentageCalculate(int price) {
        return (price / 100) * 20;
    }
}
