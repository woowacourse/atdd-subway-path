package wooteco.subway.domain.strategy.fare.age;

public class TeenAgeStrategy implements FareDiscountAgeStrategy {

    @Override
    public int discountAge(int totalAmount) {
        return ((int) (Math.ceil(totalAmount - 350) * 0.2) / 10 * 10);
    }
}
