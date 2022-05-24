package wooteco.subway.domain.strategy.fare.age;

public interface FareDiscountAgeStrategy {

    boolean isApplied(int age);

    int calculateDiscount(int totalAmount);
}
