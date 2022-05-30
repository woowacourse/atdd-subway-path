package wooteco.subway.domain.discountstrategy;

public class DefaultDiscountStrategy implements AgeDiscountStrategy {
    @Override
    public double calculateFare(int fare) {
        return fare;
    }
}
