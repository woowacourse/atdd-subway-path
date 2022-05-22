package wooteco.subway.domain.strategy.fare.age;

public class AdultStrategy implements FareDiscountAgeStrategy {

    @Override
    public int discountAge(int totalAmount) {
        return 0;
    }
}
