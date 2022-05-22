package wooteco.subway.domain.strategy.fare.age;

public class InfantStrategy implements FareDiscountAgeStrategy {

    @Override
    public int discountAge(int totalAmount) {
        return totalAmount;
    }
}
