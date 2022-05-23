package wooteco.subway.domain.strategy;

public class AdultDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
