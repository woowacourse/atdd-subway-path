package wooteco.subway.domain.discountstrategy;

public class TeenagerDiscountStrategy implements AgeDiscountStrategy {
    @Override
    public double calculateFare(int fare) {
        return (fare - 350) * 0.2;
    }
}
