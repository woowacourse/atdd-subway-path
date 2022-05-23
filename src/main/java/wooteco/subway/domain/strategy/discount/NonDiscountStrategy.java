package wooteco.subway.domain.strategy.discount;

public class NonDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
