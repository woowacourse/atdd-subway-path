package wooteco.subway.domain.strategy.discount;

public class BabyDiscountStrategy implements FareDiscountStrategy {

    @Override
    public int calculateDiscount(int fare) {
        return fare;
    }
    
}
