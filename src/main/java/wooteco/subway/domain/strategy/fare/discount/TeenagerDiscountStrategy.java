package wooteco.subway.domain.strategy.fare.discount;

public class TeenagerDiscountStrategy implements DiscountStrategy{

    @Override
    public int calculateDiscount(int price) {
        return (int) ((price - 350) * 0.2);
    }
}
