package wooteco.subway.domain.strategy.discount;

public class AdultDiscountStrategy implements FareDiscountStrategy {

    @Override
    public int calculateDiscount(int fare) {
        return 0;
    }
    
}
