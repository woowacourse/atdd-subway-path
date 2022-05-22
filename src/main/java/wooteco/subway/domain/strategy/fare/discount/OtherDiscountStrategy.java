package wooteco.subway.domain.strategy.fare.discount;

public class OtherDiscountStrategy implements DiscountStrategy{
    @Override
    public int calculateDiscount(int price) {
        return 0;
    }
}
