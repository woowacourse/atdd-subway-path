package wooteco.subway.domain.path.strategy;

public class FreeDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        return 0;
    }
}
