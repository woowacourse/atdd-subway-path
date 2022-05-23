package wooteco.subway.domain.fare.policy.age;

public class AdultDiscountPolicy implements AgeDiscountPolicy {
    public AdultDiscountPolicy() {
    }

    @Override
    public double calculate(double baseFare) {
        return baseFare;
    }
}
