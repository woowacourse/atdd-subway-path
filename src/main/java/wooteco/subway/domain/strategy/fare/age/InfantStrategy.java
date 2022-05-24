package wooteco.subway.domain.strategy.fare.age;

public class InfantStrategy implements FareDiscountAgeStrategy {

    @Override
    public boolean isApplied(int age) {
        return age >= 0 && age < 6;
    }

    @Override
    public int calculateDiscount(int totalAmount) {
        return totalAmount;
    }
}
