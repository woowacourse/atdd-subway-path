package wooteco.subway.domain.fare;

public class FareCalculator {

    private final FarePolicy farePolicy;
    private final AgeDiscountPolicy ageDiscountPolicy;

    public FareCalculator(FarePolicy farePolicy, AgeDiscountPolicy ageDiscountPolicy) {
        this.farePolicy = farePolicy;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public Fare calculate(FareCondition fareCondition) {
        Fare fare = farePolicy.calculateFare(fareCondition.getDistance(), fareCondition.getLine());
        return ageDiscountPolicy.discountFare(fare, fareCondition.getAge());
    }
}
