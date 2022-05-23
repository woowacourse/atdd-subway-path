package wooteco.subway.domain.fare;

import org.springframework.stereotype.Component;

import wooteco.subway.domain.path.Path;

@Component
public class Fare {
    private final DistanceFarePolicy fareStrategy;
    private final AgeDiscountPolicy discountStrategy;

    public Fare(DistanceFarePolicy fareStrategy, AgeDiscountPolicy discountStrategy) {
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
