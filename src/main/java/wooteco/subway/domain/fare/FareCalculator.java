package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.strategy.discount.DiscountStrategy;
import wooteco.subway.domain.fare.strategy.fare.FareStrategy;

public class FareCalculator {

    private final FareStrategy fareStrategy;
    private final DiscountStrategy discountStrategy;

    public FareCalculator(FareStrategy fareStrategy, DiscountStrategy discountStrategy) {
        this.fareStrategy = fareStrategy;
        this.discountStrategy = discountStrategy;
    }

    public int calculate(double distance, int extraFare) {
        int originFare = fareStrategy.calculate(distance, extraFare);
        return discountStrategy.getDiscountedFare(originFare);
    }
}
