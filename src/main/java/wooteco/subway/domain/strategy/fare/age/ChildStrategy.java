package wooteco.subway.domain.strategy.fare.age;

public class ChildStrategy implements FareDiscountAgeStrategy {

    @Override
    public int discountAge(int totalAmount) {
        return ((int) (Math.ceil(totalAmount - 350) * 0.5) / 10 * 10);
    }
}
