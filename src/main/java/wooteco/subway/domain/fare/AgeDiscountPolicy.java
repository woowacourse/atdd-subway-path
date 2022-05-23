package wooteco.subway.domain.fare;

public interface AgeDiscountPolicy extends FarePolicy {

    @Override
    double calculate(double baseFare);
}
