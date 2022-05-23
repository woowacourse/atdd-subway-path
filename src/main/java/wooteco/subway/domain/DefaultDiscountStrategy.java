package wooteco.subway.domain;

public class DefaultDiscountStrategy implements AgeDiscountStrategy {
    @Override
    public double calculateFare(int fare) {
        return fare;
    }
}
