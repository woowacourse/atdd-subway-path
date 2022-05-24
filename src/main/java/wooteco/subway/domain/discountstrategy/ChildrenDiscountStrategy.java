package wooteco.subway.domain.discountstrategy;

public class ChildrenDiscountStrategy implements AgeDiscountStrategy {
    @Override
    public double calculateFare(int fare) {
        return (fare - 350) * 0.5;
    }
}
