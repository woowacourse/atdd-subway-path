package wooteco.subway.domain.path.strategy;

public class NonDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        return fare;
    }
}
