package wooteco.subway.domain.strategy.discount;

public class FreeDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        return 0;
    }
}
