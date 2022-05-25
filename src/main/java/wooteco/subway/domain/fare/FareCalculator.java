package wooteco.subway.domain.fare;

import wooteco.subway.domain.path.Path;

public class FareCalculator {
    public static int calculate(Path path, int age) {
        int distance = path.getShortestDistance();
        int fare = DistanceFareCalculator.calculateFare(distance);
        fare += path.getExtraFare();
        return AgeDiscountCalculator.calculateDiscount(age, fare);
    }
}
