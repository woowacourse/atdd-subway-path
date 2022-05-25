package wooteco.subway.domain.strategy.fare.age;

public class AdultStrategy implements FareDiscountAgeStrategy {

    @Override
    public boolean isApplied(int age) {
        return age > 19;
    }

    @Override
    public int calculateDiscount(int totalAmount) {
        return 0;
    }
}
