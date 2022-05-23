package wooteco.subway.domain.fare;

import wooteco.subway.domain.Path;

public class Fare {
    private final FareStrategy fareStrategy;
    private final DiscountStrategy discountStrategy;

    public Fare(FareStrategy fareStrategy, DiscountStrategy discountStrategy) {
        this.fareStrategy = fareStrategy;
        this.discountStrategy = discountStrategy;
    }

    public int calculate(Path path, int age) {
        int distance = path.getShortestDistance();
        int fare = fareStrategy.calculate(distance);
        fare += path.getExtraFare();
        return discountStrategy.discount(fare, age);
    }
}
