package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.strategy.fare.FareStrategy;

public class FareCalculator {

    private final FareStrategy fareStrategy;
    private final AgeDiscount ageDiscount;

    public FareCalculator(FareStrategy fareStrategy, AgeDiscount ageDiscount) {
        this.fareStrategy = fareStrategy;
        this.ageDiscount = ageDiscount;
    }

    public int calculate(double distance, int extraFare) {
        int originFare = fareStrategy.calculate(distance, extraFare);
        return ageDiscount.getDiscountedFare(originFare);
    }
}
