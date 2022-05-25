package wooteco.subway.domain.fare;

public class FareCalculator {

    private final FarePolicy farePolicy;
    private final DiscountPolicy discountPolicy;

    public FareCalculator(FarePolicy farePolicy, DiscountPolicy discountPolicy) {
        this.farePolicy = farePolicy;
        this.discountPolicy = discountPolicy;
    }

    public Fare calculate(FareCondition fareCondition) {
        Fare fare = farePolicy.calculateFare(fareCondition.getDistance(), fareCondition.getLine());
        return discountPolicy.discountFare(fare, fareCondition.getAge());
    }
}
